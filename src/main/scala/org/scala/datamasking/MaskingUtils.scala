package org.scala.datamasking

import com.privacylogistics.FF3Cipher

object MaskingUtils {

  def applyFF3Masking(key:String, tweak:String, value:String, customeAlphabet: Option[String]=None):String = {
    val cipher = customeAlphabet match {
      case Some(alphabet) => new FF3Cipher(key,tweak,alphabet)
      case None => new FF3Cipher(key,tweak)
    }
    cipher.encrypt(value)
  }

}
