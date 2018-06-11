package main;

import encapsulacion.Articulo;
import encapsulacion.Comentario;
import encapsulacion.Etiqueta;
import encapsulacion.Usuario;
import freemarker.template.Configuration;
import freemarker.template.Version;
import modelo.servicios.EntityServices.ArticuloService;
import modelo.servicios.EntityServices.ComentarioService;
import modelo.servicios.EntityServices.EtiquetaService;
import modelo.servicios.EntityServices.UsuarioService;
import modelo.servicios.Utils.BootStrapService;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) throws SQLException {

        BootStrapService.startDb();

        BootStrapService.crearTablas();
        
        Usuario usuario = new Usuario(2l, "admin", "juan", "1234", false, true);


        UsuarioService usuarioService = new UsuarioService();
//        usuarioService.insert(usuario);
        //usuarioService.delete(usuario);
        //usuarioService.update(usuario);

//        Usuario usuario = usuarioService.getById(1L);
//        System.out.println(usuario.getNombre());

        ArticuloService  articuloService = new ArticuloService();
//        articuloService.insert(articulo);

        EtiquetaService etiquetaService = new EtiquetaService();
//        etiquetaService.insert(etiqueta);

        ComentarioService comentarioService = new ComentarioService();





        staticFiles.location("/templates");

        Configuration configuration = new Configuration(new Version(2, 3, 0));
        configuration.setClassForTemplateLoading(Main.class, "/templates");

        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);


//        get("/", (request, response) -> {
//            Map<String, Object> attributes = new HashMap<>();
//            return new ModelAndView(attributes, "login.ftl");
//        }, freeMarkerEngine);

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Inicio");

            attributes.put("etiquetas", etiquetaService.getAll());
            attributes.put("list", articuloService.getAll());

            return new ModelAndView(attributes, "inicio.ftl");
        }, freeMarkerEngine);


        get("/verMas/:id", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String idArticulo = request.params("id");

            Articulo articulo2 = articuloService.getById(Integer.parseInt(idArticulo));
            attributes.put("articulo", articulo2);
            attributes.put("comentarios", comentarioService.getByArticulo(Integer.parseInt(idArticulo)));

            return new ModelAndView(attributes, "post.ftl");
        }, freeMarkerEngine);


        post("/agregarComentario", (request, response) -> {

            String comentario = request.queryParams("comentario");
            String articulo = request.queryParams("articulo");
            String autor = request.queryParams("autor");

            Usuario usuario1 = usuarioService.getById(Integer.parseInt(autor));
            Articulo articulo1 = articuloService.getById(Integer.parseInt(articulo));

            comentarioService.insert(new Comentario(comentario, usuario1, articulo1));



            response.redirect("/verMas/" + articulo);
            return  "";
        });

        get("/agregarPost", (request, response) -> configuration.getTemplate("agregarPost.ftl"));


        get("/guardarPost", (request, response) -> {



            return "";
        });




    }
}
