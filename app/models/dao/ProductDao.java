package models.dao;

import java.io.File;
import java.util.List;

import com.avaje.ebean.annotation.Transactional;

import models.Product;
import play.Play;
import play.data.Form;
import views.form.ProductForm;

@SuppressWarnings("deprecation") //For FilePath of upload method AND delete method
public class ProductDao {
	/**
	 * 全データ取得
	 * @return List<Product>
	 */
	public List<Product> findALL() {
		List<Product> product = Product.finder.all();
		return product;
	}
	/**
	 * 指定されたidのデータ取得
	 * @param id
	 * @return Product
	 */
	public Product findById(Long id) {
		Product product = Product.finder.where().eq("id", id).findUnique();
		return product;
	}
	
	/**
	 * 入力データ保存
	 * @param productForm
	 */
	@Transactional
	public void save(Form<ProductForm> productForm){
		Product product = Product.convertToModel(productForm.get());
		if(findById(productForm.get().id) != null) {
			product.update();
		}
		product.save();
	}
	
	
	/**
	 * 指定した情報を削除する
	 * @param product
	 */
	@Transactional
	public void delete(Product product){
		if (product.file != null){
			String existingFile = product.getFile();
			String filePath = Play.application().path().getAbsolutePath() + Play.application().configuration().getString("app.uploadPath");
			new File(filePath + "/" + existingFile).delete();
		}
		product.delete();
	}
	
	/**
	 * 指定された情報を更新する
	 * @param editForm
	 */
	public void update(ProductForm editForm){
		Product product = findById(editForm.id);
		if(editForm.title == null) {
			editForm.title = product.title;
		}
		if(product != null && editForm.file == null) {
			editForm.file = product.file;
		}
		Product editProduct= Product.convertToModel(editForm);
		editProduct.update();
	}
	
}