package models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import views.form.ProductForm;

@Entity(name = "product")
public class Product extends Model {
	
	@Id
	@Column(name = "id", length = 10)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	@Column(length = 100)
	public String title;
	
	@Column(length = 500)
	public String description;
	
	@Column(length = 10)
	public Long price;
	
	@Column(length = 20)
	public String file;
	
	
	public Product() {}
	
	public Product(Long id, String title, String description, Long price, String file) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.file = file;
	}
	
	public static Find<Long, Product> finder = new Find<Long, Product>(){};
		
	public Long getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Long getPrice() {
		return price;
	}
	
	public String getFile(){
		return file;
	}
	
	public static Product convertToModel(ProductForm form) {
		Product product = new Product();
		product.id = form.id;
		product.title = form.title;
		product.description = form.description;
		product.price = form.price;
		product.file = form.file;
		return product;
	}

}
