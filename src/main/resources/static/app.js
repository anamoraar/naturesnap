$(document).ready(function () {
  //Función para crear una nueva imagen
  $("#submitImage").on("click", function () {
    //Se deshabilita el botón mientras se van a checkear los datos
    $("#submitImage").prop("disabled", true);
    //la data se pasa al servidor por medio de un Post Request con lo necesario para crear la imagen
    var form = $("#form").serialize();
    var data = new FormData($("#form")[0]); //"(#form)[0]" contiene el archivo
    //Se obtienen los datos del form
    var file = $("#file").val();
    var description = $("#description").val();
    var ownerID = $("#ownerID").val();
    var authorID = $("#authorID").val();
    var keywords = $("#keywords").val();
    var license = $("#license").val();
    //Se insertan los datos que se van a pasar a "/image/uploadimage" para hacer el Post de la imagen
    data.append("description", description);
    data.append("ownerID", ownerID);
    data.append("authorID", authorID);
    data.append("keywords", keywords);
    data.append("license", license);
    //Se verifica que los campos obligatorios se encuentren llenos para hacer enviar los datos al servidor
    if (
      file === "" ||
      description === "" ||
      ownerID === "" ||
      authorID === ""
    ) {
      $("#submitImage").prop("disabled", false);
      $("#file").css("border-color", "red");
      $("#description").css("border-color", "red");
      $("#ownerID").css("border-color", "red");
      $("#authorID").css("border-color", "red");
      $("#error_file").html("Llene el campo indicado.");
      $("#error_description").html("Llene el campo indicado.");
      $("#error_ownerID").html("Llene el campo indicado.");
      $("#error_authorID").html("Llene el campo indicado.");
    } else {
      $("#file").css("border-color", "");
      $("#description").css("border-color", "");
      $("#ownerID").css("border-color", "");
      $("#authorID").css("border-color", "");
      $("#keywords").css("border-color", "");
      $("#error_file").css("opacity", 0);
      $("#error_description").css("opacity", 0);
      $("#error_ownerID").css("opacity", 0);
      $("#error_authorID").css("opacity", 0);
      $.ajax({
        type: "POST",
        enctype: "multipart/form-data",
        data: data,
        url: "/image/uploadimage",
        processData: false,
        contentType: false,
        cache: false,
        success: function (data, statusText, xhr) {
          console.log(xhr.status);
          if (xhr.status == "200") {
            $("#form")[0].reset();
            $("#error").text("");
            $("#success").css("display", "block");
            $("#success").html("Image uploaded");
            $("#success").delay(2000).fadeOut("slow");
          }
        },
        error: function (e) {
          $("#error").css("display", "block");
          $("#error").html("Input error, owner or author may not exist");
          $("#error")
            .delay(3000)
            .fadeOut("slow", function () {
              setTimeout(function () {
                location.reload();
              }, 1000);
            });
        },
      });
    }
  });
  //Función para crear un nuevo taxón
  $("#submitTaxon").on("click", function () {
    //Se deshabilita el botón mientras se van a checkear los datos
    $("#submitTaxon").prop("disabled", true);
    //Se obtienen los datos del form
    var taxonType = $("#type").val();
    var name = $("#scname").val();
    var author = $("#author").val();
    var year = $("#year").val();
    var ancestor = $("#ancestor").val();
    //Se verifica que los campos obligatorios se encuentren llenos para hacer enviar los datos al servidor
    if (name === "" || author === "" || year === "" || ancestor === "") {
      $("#submitTaxon").prop("disabled", false);
      $("#scname").css("border-color", "red");
      $("#author").css("border-color", "red");
      $("#year").css("border-color", "red");
      $("#ancestor").css("border-color", "red");
      $("#error_scname").html("Llene el campo indicado.");
      $("#error_author").html("Llene el campo indicado.");
      $("#error_year").html("Llene el campo indicado.");
      $("#error_ancestor").html("Llene el campo indicado.");
    } else {
      $("#scname").css("border-color", "");
      $("#author").css("border-color", "");
      $("#year").css("border-color", "");
      $("#ancestor").css("border-color", "");
      $("#error_scname").css("opacity", 0);
      $("#error_author").css("opacity", 0);
      $("#error_year").css("opacity", 0);
      $("#error_ancestor").css("opacity", 0);
      $.ajax({
        type: "POST",
        data: {
          taxonType: taxonType,
          name: name,
          author: author,
          year: year,
          ancestor: ancestor,
        },
        url: "/taxons/createtaxon",
        success: function (data) {
          $("#form2")[0].reset();
          $("#error2").text("");
          $("#success2").css("display", "block");
          $("#success2").text("Taxon created");
          $("#success2").delay(2000).fadeOut("slow");
        },
        error: function (e) {
          $("#error2").css("display", "block");
          $("#error2").text("Input error, check ancestor");
          $("#error2")
            .delay(3000)
            .fadeOut("slow", function () {
              setTimeout(function () {
                location.reload();
              }, 1000);
            });
        },
      });
    }
  });
  //Función para eliminar una imagen
  $(".deleteImage").click(function () {
    var imageId = $(this).data("id");
    if (confirm("Are you sure you want to delete this image?")) {
      $.ajax({
        url: "/image/delete/" + imageId,
        type: "DELETE",
        success: function (e) {
          $("body").fadeOut(500, function () {
            location.reload();
          });
        },
        error: function (xhr, textStatus) {
          alert("There was a problem deleting the image");
        },
      });
    }
  });
  //Función para eliminar un taxón
  $(".deleteTaxon").click(function () {
    var taxonId = $(this).data("id");
    if (confirm("Are you sure you want to delete this taxon?")) {
      $.ajax({
        url: "/taxons/delete/" + taxonId,
        type: "DELETE",
        success: function (e) {
          $("body").fadeOut(500, function () {
            location.reload();
          });
        },
        error: function (xhr, textStatus) {
          alert("Ancestor taxon can't be deleted before its children");
        },
      });
    }
  });
  //Función para editar una imagen existente
  $("#editImage").on("click", function () {
    //Se deshabilita el botón mientras se van a enviar los datos
    $("#editImage").prop("disabled", true);
    //El id de la imagen que se quiere editar
    var imageId = $(this).data("id");
    //Se obtienen los datos del form
    var description = $("#description2").val();
    var ownerID = $("#ownerID2").val();
    var authorID = $("#authorID2").val();
    var keywords = $("#keywords2").val();
    var license = $("#license2").val();
    //Se envía el PUT request al servidor en el endpoint "/image/update/" + imageId,
    $.ajax({
      type: "PUT",
      url: "/image/update/" + imageId,
      data: {
        description: description,
        ownerID: ownerID,
        authorID: authorID,
        keywords: keywords,
        license: license,
      },
      success: function (data, statusText, xhr) {
        $("#form3")[0].reset();
        $("#error3").text("");
        $("#success3").css("display", "block");
        $("#success3").text("Image updated");
        $("#success3").delay(2000).fadeOut("slow");
      },
      error: function (e) {
        $("#error3").css("display", "block");
        $("#error3").text("Input error, owner or author may not exist");
        $("#error3")
          .delay(3000)
          .fadeOut("slow", function () {
            setTimeout(function () {
              location.reload();
            }, 500);
          });
      },
    });
  });
  //Función para editar un taxón existente
  $("#editTaxon").on("click", function () {
    //Se deshabilita el botón mientras se van a enviar los datos
    $("#editTaxon").prop("disabled", true);
    //El id del taxón que se quiere editar
    var taxonId = $(this).data("id");
    //Se obtienen los datos del form
    var name = $("#scname2").val();
    var author = $("#author2").val();
    var year = $("#year2").val();
    var ancestor = $("#ancestor2").val();
    //Se envía el PUT request al servidor en el endpoint "/taxons/update/"+taxonId
    $.ajax({
      type: "PUT",
      url: "/taxons/update/" + taxonId,
      data: {
        name: name,
        author: author,
        year: year,
        ancestor: ancestor,
      },
      success: function (data, statusText, xhr) {
        $("#form4")[0].reset();
        $("#error4").text("");
        $("#success4").css("display", "block");
        $("#success4").text("Taxon updated");
        $("#success4").delay(2000).fadeOut("slow");
      },
      error: function (e) {
        $("#error4").css("display", "block");
        $("#error4").text("Input error, check ancestor");
        $("#error4")
          .delay(3000)
          .fadeOut("slow", function () {
            setTimeout(function () {
              location.reload();
            }, 500);
          });
      },
    });
  });
});
