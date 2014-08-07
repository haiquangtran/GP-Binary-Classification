package examples.gp;

public class CancerPatient {
//	   id. Sample code number            id number
//	   a1. Clump Thickness               1 - 10
//	   a2. Uniformity of Cell Size       1 - 10
//	   a3. Uniformity of Cell Shape      1 - 10
//	   a4. Marginal Adhesion             1 - 10
//	   a5. Single Epithelial Cell Size   1 - 10
//	   a6. Bare Nuclei                   1 - 10
//	   a7. Bland Chromatin               1 - 10
//	   a8. Normal Nucleoli               1 - 10
//	  a9. Mitoses                       1 - 10
	private int id;
	private int a1;
	private int a2;
	private int a3;
	private int a4;
	private int a5;
	private int a6;
	private int a7;
	private int a8;
	private int a9;
	
	
	public CancerPatient(int id, int a1, int a2, int a3, int a4, 
			int a5, int a6, int a7,int a8, int a9){
		this.setId(id);
		this.setA1(a1);
		this.setA2(a2);
		this.setA3(a3);
		this.setA4(a4);
		this.setA5(a5);
		this.setA6(a6);
		this.setA7(a7);
		this.setA8(a8);
		this.setA9(a9);
	}
	
	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}

	public int getA1() {
		return a1;
	}

	private void setA1(int a1) {
		this.a1 = a1;
	}

	public int getA2() {
		return a2;
	}

	private void setA2(int a2) {
		this.a2 = a2;
	}

	public int getA3() {
		return a3;
	}

	private void setA3(int a3) {
		this.a3 = a3;
	}

	public int getA4() {
		return a4;
	}

	private void setA4(int a4) {
		this.a4 = a4;
	}

	public int getA5() {
		return a5;
	}

	private void setA5(int a5) {
		this.a5 = a5;
	}

	public int getA6() {
		return a6;
	}

	private void setA6(int a6) {
		this.a6 = a6;
	}

	public int getA7() {
		return a7;
	}

	private void setA7(int a7) {
		this.a7 = a7;
	}

	public int getA8() {
		return a8;
	}

	private void setA8(int a8) {
		this.a8 = a8;
	}

	public int getA9() {
		return a9;
	}

	private void setA9(int a9) {
		this.a9 = a9;
	}

	@Override
	public String toString() {

		return String.format("%id,%d,%d,%d,%d,%d,%d,%d,%d,%d", id,a1,a2,a3,a4,a5,a6,a7,a8,a9);
	}
	
	
}
