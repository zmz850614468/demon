/*
 * This file is licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demon.tool.ftp_ssh;

import com.jcraft.jsch.UserInfo;

/**
* A patch to jsch.UserInfo to allow passphrase
*/
public class JSchUserInfo implements UserInfo{

		private String password;
		private String passphrase;
		
		public JSchUserInfo(String password, String passphrase) {
			this.password = password;
			this.passphrase = passphrase;
		}

		public String getPassphrase() {
			return this.passphrase;
		}

		public String getPassword() {
			return this.password;
		}

		public boolean promptPassphrase(String s) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean promptPassword(String s) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean promptYesNo(String s) {
			// TODO Auto-generated method stub
			return false;
		}

		public void showMessage(String s) {
			// TODO Auto-generated method stub
			
		}

}
