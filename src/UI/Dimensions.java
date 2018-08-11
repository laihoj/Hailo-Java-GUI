package UI;

/***********************************************************************************************/
public class Dimensions {
	public int[] dims;

	public Dimensions(int a) {
		this.dims = new int[] { a, 0 };
	}

	public Dimensions(int a, int b) {
		this.dims = new int[] { a, b };
	}

	public Dimensions(int... dims) {
		this.dims = dims;
	}
	
	public Dimensions() {
		this.dims = new int[] { 0, 0 };
	}

	public Dimensions(Dimensions dimensions) {
		this.dims = new int[dimensions.dims.length];
		for (int i = 0; i < dimensions.dims.length; i++) {
			this.dims[i] = dimensions.dims[i];
		}
		// this.dims = dimensions.dims;
	}
}

/***********************************************************************************************/