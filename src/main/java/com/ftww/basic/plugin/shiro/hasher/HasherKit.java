package com.ftww.basic.plugin.shiro.hasher;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

public class HasherKit {

	/*
	 * 密码服务
	 */
	private static PasswordService passwordService = new DefaultPasswordService();

	public static HasherInfo hash(String hashText) {
		return hash(hashText, Hasher.DEFAULT);
	}

	public static HasherInfo hash(String hashText, Hasher hasher) {
		HasherInfo hasherInfo = null;
		if (hasher == Hasher.DEFAULT) {
			/*
			 * 参数hashText：密码
			 * 方法encryptPassword()：加密
			 */
			hasherInfo = new HasherInfo(hashText,passwordService.encryptPassword(hashText), hasher, "");
		}
		return hasherInfo;
	}

	public static boolean match(Object submittedPlaintext, String encrypted) {
		return match(submittedPlaintext, encrypted, Hasher.DEFAULT);
	}

	public static boolean match(Object submittedPlaintext, String encrypted,Hasher hasher) {
		boolean result = false;
		if (hasher == Hasher.DEFAULT) {
			/*
			 * 参数submittedPlaintext：提交的明文
			 * 参数encrypted：密文
			 * 方法passwordsMatch()：对比
			 */
			result = passwordService.passwordsMatch(submittedPlaintext,	encrypted);
		}
		return result;
	}

}
