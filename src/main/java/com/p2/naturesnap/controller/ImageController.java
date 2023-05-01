package com.p2.naturesnap.controller;

import com.p2.naturesnap.model.*;
import com.p2.naturesnap.repository.ImageRepository;
import com.p2.naturesnap.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//Clase que responde a las solicitudes HTTP para las acciones relacionadas a las imágenes
@RestController
public class ImageController {

    //Inyectar el repositorio para realizar operaciones CRUD con las imágenes
    @Autowired
    private ImageRepository imageRepository;

    //Inyectar el repositorio para encontrar los owners indicados a la hora de subir una imagen
    @Autowired
    private OwnerRepository ownerRepository;

    //path a la carpeta donde se guardan las imágenes
    public static String storagePath = "C:\\Users\\anamo\\IdeaProjects\\naturesnap\\src\\main\\resources\\storage";

    //Obtiene todas las imágenes en la base de datos, se las pasa al modelo y
    //retorna la vista "index", donde se despliegan las imágenes del sistema
    @GetMapping(value = {"/", "/home"})
    public ModelAndView showAllImages() {
        ModelAndView modelView = new ModelAndView();
        List<Image> images = imageRepository.findAll();
        modelView.addObject("images", images);
        modelView.setViewName("index.html");
        return modelView;
    }

    //Retorna la vista que tiene al Form para subir una nueva imagen
    @GetMapping("/image/addimage")
    public ModelAndView imageForm(){
        ModelAndView modelView = new ModelAndView();
        modelView.setViewName("imageform.html");
        return modelView;
    }


    //Endpoint para crear una nueva imagen. Se espera un archivo, una descripción, un ownerID,
    //un authorID, keywords y una licencia
    @PostMapping("/image/uploadimage")
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file, @RequestParam String description,
                                              @RequestParam int ownerID, @RequestParam int authorID,
                                              @RequestParam String keywords, @RequestParam License license) throws IOException
    {
        try {
            //El nombre de la imagen
            String name = StringUtils.cleanPath(file.getOriginalFilename());
            //El path donde se va a guardar la nueva imagen
            String imagePath = storagePath+"\\"+name;
            //Guardar la imagen en la carpeta
            file.transferTo(new File(imagePath));
            //Guardar las keywords en una lista
            List<String> keywordsList = Arrays.asList(keywords.split("\\s*,\\s*"));
            //Se guarda en un array para evitar repeticiones en la description
            String[] descriptions = description.split(",");
            //Instanciar la imagen y luego setearle los atributos
            Image image = new Image();
            image.setDescription(descriptions[0]);
            image.setPath(imagePath);
            image.setKeywords(keywordsList.subList(0, keywordsList.size()/2));
            image.setLicense(license);
            //Obtener el owner y author indicados, si alguno no existe se retorna un error
            Optional<Owner> owner = ownerRepository.findOwnerById(ownerID);
            if(owner.isPresent()) image.setOwner(owner.get());
            else return ResponseEntity.badRequest().body("Error.");
            Optional<Owner> author = ownerRepository.findOwnerById(authorID);
            if(author.isPresent()) image.setAuthor((Person) author.get());
            else return ResponseEntity.badRequest().body("Error.");
            //Guardar la instancia en la base de datos
            imageRepository.save(image);
            return ResponseEntity.ok("Imagen cargada");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error");
        }
    }

    //Endpoint para obtener la imagen guardada en un path dado el id de la instancia, en caso de
    //que se pueda obtener retorna la imagen en el ResponseEntity. Si no, retorna un error
    @GetMapping("/image/vista/{id}")
    public ResponseEntity<?> showImage(@PathVariable("id") int id) throws IOException{
        Optional<Image> imagenEntidad = imageRepository.findImageById(id);
        //El path que lleva hasta la imagen si esta existe
        String imagePath = imagenEntidad.get().getPath();
        try{
            //Obtener la extensión de la imagen
            String extension = imagePath.substring(imagePath.indexOf(".")+1);
            //Cargar la imagen y pasarla en el cuerpo del ResponseEntity
            byte[] imagen = Files.readAllBytes(new File(imagePath).toPath());
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/"+extension)).body(imagen);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error");
        }
    }

    //Dado el id de una imagen, pasa sus atributos al modelView y muestra la vista "imageattributes"
    @GetMapping("/image/attributes")
    public ModelAndView showAttributes(@RequestParam int id) {
        ModelAndView modelView = new ModelAndView();
        Optional<Image> imagenOp = imageRepository.findImageById(id);
        if (imagenOp.isPresent()) { //si la imagen existe
            Image image = imagenOp.get();
            //Agregar los atributos de la imagen para mostrarlos en la vista
            modelView.addObject("id", id);
            modelView.addObject("description", image.getDescription());
            modelView.addObject("creationDate", image.getCreation_date().toString());
            modelView.addObject("owner", image.getOwner().getName());
            modelView.addObject("author", image.getAuthor().displayInformation());
            modelView.addObject("license", image.getLicense());
            modelView.addObject("keywords", image.displayInformation());
        }
        modelView.setViewName("imageattributes.html"); //se retorna la vista "imageattributes"
        return modelView;
    }

    //Elimina una imagen dado su id, retorna un ResponseEntity con el estado de la acción
    @DeleteMapping("/image/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") int id) {
        Optional<Image> imagen = imageRepository.findImageById(id);
        if (imagen.isPresent()) {
            imageRepository.delete(imagen.get());
            return ResponseEntity.ok("Image deleted.");
        }
        return ResponseEntity.badRequest().body("Error.");
    }

    //Retorna la vista que tiene el Form para editar una imagen existente. A dicha vista
    //se le agrega el id de la imagen que se quiere editar
    @GetMapping("/image/edit")
    public ModelAndView editImageForm(@RequestParam int id){
        ModelAndView modelView = new ModelAndView();
        Optional<Image> imageOptional = imageRepository.findImageById(id);
        if(imageOptional.isPresent())
            modelView.addObject("id", id);
        modelView.setViewName("editimage.html");
        return modelView;
    }

    //Modifica los atributos de una imagen que el usuario ingresó en el form. Retorna ResponseEntity.ok()
    //si no hay problemas o ResponseEntity.badRequest() de lo contrario
    @PutMapping("/image/update/{id}")
    public ResponseEntity<?> editImage(@PathVariable("id") int id, @RequestParam String description,
                                       @RequestParam String ownerID, @RequestParam String authorID,
                                       @RequestParam String keywords, @RequestParam License license){
        Optional<Image> imageOptional = imageRepository.findImageById(id);
        if(imageOptional.isPresent()){
            Image image = imageOptional.get();
            //Solamente se cambian los atributos que no están vacíos
            if(!description.equals("")) image.setDescription(description);
            if(!ownerID.equals("")) {
                Optional<Owner> owner = ownerRepository.findOwnerById(Integer.parseInt(ownerID));
                if(owner.isPresent()) image.setOwner(owner.get());
                else return ResponseEntity.badRequest().body("Error.");
            }
            if(!authorID.equals("")) {
                Optional<Owner> author = ownerRepository.findOwnerById(Integer.parseInt(authorID));
                if(author.isPresent()) image.setAuthor((Person) author.get());
                else return ResponseEntity.badRequest().body("Error.");
            }
            if(!keywords.equals("")) {
                List<String> keywordsList = Arrays.asList(keywords.split("\\s*,\\s*"));
                image.setKeywords(keywordsList.subList(0, keywordsList.size()/2));
            }
            if(!license.equals("")) image.setLicense(license);
            imageRepository.save(image);
            return ResponseEntity.ok("Image updated");
        }
        return ResponseEntity.badRequest().body("Error.");
    }


}
