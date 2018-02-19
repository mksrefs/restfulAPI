package views.form;

import javax.validation.constraints.Max;

import play.data.validation.Constraints;

public class ProductForm {
	
	@Max(999999999)
	public Long id;
	
	@Constraints.MaxLength(100)
	public String title;
	
	@Constraints.MaxLength(500)
	public String description;
	
	@Max(999999999)
	public Long price;
	@Constraints.MaxLength(10)
	public String file;
	
	public ProductForm() {}
	
	public ProductForm(Long id, String title, String description, Long price, String file) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.file = file;
	}
}
