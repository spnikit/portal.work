package sheff.rjd.services.repair;

public class SignInfoObject {
		/**
		 * @param sign_num - номер подписи
		 * @param binary - бинарник подиписи
		 */
		public SignInfoObject(int sign_num, byte[] binary) {
				this.sign_num = sign_num;
				this.binary = binary;
			}
		
		
		
			public SignInfoObject(int sign_num, byte[] binary, String fio,
				String date) {
			super();
			this.sign_num = sign_num;
			this.binary = binary;
			this.fio = fio;
			this.date = date;
		}



			int sign_num;
			byte[] binary;
			String fio;
			String date;
			public int getSign_num() {
				return sign_num;
			}
			public void setSign_num(int sign_num) {
				this.sign_num = sign_num;
			}
			public byte[] getBinary() {
				return binary;
			}
			public void setBinary(byte[] binary) {
				this.binary = binary;
			}
			public String getFio() {
				return fio;
			}
			public void setFio(String fio) {
				this.fio = fio;
			}
			public String getDate() {
				return date;
			}
			public void setDate(String date) {
				this.date = date;
			}
			
		}

