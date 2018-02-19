package controllers;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import models.Product;
import models.dao.ProductDao;

import com.google.inject.Inject;

import play.Configuration;
import play.Play;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.form.ProductForm;

import java.io.File;
import java.text.SimpleDateFormat;

@SuppressWarnings("deprecation") //For upload method FilePath
public class Application extends Controller {
	
	@Inject
	FormFactory formFactory;
	
	ProductDao productDao = new ProductDao();

	public Result index() {
		List<Product> product = productDao.findALL();
		if(product == null) {
			return notFound("no data");
		}
		return ok(Json.toJson(product));
	}
	
	@Valid
	public Result detail(Long id) {
		if (id == null || id == 0L) {
			return badRequest("invalid value");
		}
		Product product = productDao.findById(id);
		if (product == null){
			return notFound("no data");
		}
		return ok(Json.toJson(product));
	}
	
	@Valid
	public Result create() {
		Form<ProductForm> productForm = formFactory.form(ProductForm.class).bindFromRequest();
		if (productForm.hasErrors()) {
			return badRequest(productForm.errorsAsJson());
		}
		if (productForm.get().file != null) {
			productForm.get().file = null; // fileはurlから登録させない
		}
		if(request().body().asMultipartFormData() != null) {
			upload(productForm.get());
		}
		productDao.save(productForm);
		return index();
	}
	
	@Valid
	public Result delete(Long id) {
		Form<ProductForm> productForm = formFactory.form(ProductForm.class).bindFromRequest();
		if (productForm.hasErrors()) {
			return badRequest(productForm.errorsAsJson());
		}
		Product product = productDao.findById(id);
		if(product == null) {
			return badRequest("aleady not existed");
		}
		productDao.delete(product);
		return index();
	}
	
	@Valid
	public Result update(Long id){
		if (id == null || id == 0L) {
			return badRequest("incorrect id");
		}
		Form<ProductForm> productForm = formFactory.form(ProductForm.class).bindFromRequest();
		if (productForm.hasErrors()) {
			return badRequest(productForm.errorsAsJson());
		}
		if (productForm.get().file != null) {
			productForm.get().file = null; // fileはurlから登録させない
		}
		Product product = productDao.findById(id);
		if (product == null) {
			return notFound("no data");
		}
		ProductForm editForm = productForm.get();
		editForm.id = id;
		if (editForm.title == null && editForm.description == null && editForm.price == null && request().body().asMultipartFormData() == null) {
			return badRequest("data is empty");
		}
		
		// confirm existingFile
		String existingFile = null;
		if(product.file != null) {
			existingFile = product.file;
		}
		// get file
		if(request().body().asMultipartFormData() != null){
			FilePart<Object> requestFile = request().body().asMultipartFormData().getFile("image");
			if(requestFile == null) {
				return badRequest("incorrect way to send file");
			}
			if(!requestFile.getContentType().equals("image/jpeg") ) {
				return badRequest("incorrect type file");
			}
			upload(editForm);
			// delete existing file 
			if(existingFile != null) {
				Configuration conf = Play.application().configuration();
				String uploadPath = conf.getString("app.uploadPath");
				String filePath = Play.application().path().getAbsolutePath() + uploadPath;
				new File(filePath + "/" + existingFile).delete();
			}
		}
		productDao.update(editForm);
		return index();
	}
	
	public Result upload(ProductForm productForm) {
		MultipartFormData<File> body = request().body().asMultipartFormData();
		FilePart<File> picture = body.getFile("image");
		String contentType = picture.getContentType();
		if ( !contentType.equals("image/jpeg") ) {
			return badRequest("inappropriate fileType");
		}
		// to move file under project directory
		Configuration conf = Play.application().configuration();
		String uploadPath = conf.getString("app.uploadPath");
		String filePath = Play.application().path().getAbsolutePath() + uploadPath;
		// set filename
		SimpleDateFormat sdf = new SimpleDateFormat("DDDHHmmss");
		String fileName = sdf.format(new Date()) + ".jpeg";
		// import file
		File file = picture.getFile();
		file.renameTo(new File(filePath, fileName));
		ProductForm form = productForm;
		form.file = fileName;
		return ok("File uploaded");
	}
}