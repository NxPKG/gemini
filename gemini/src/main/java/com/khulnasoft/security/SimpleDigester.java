/*******************************************************************************
 * Copyright (c) 2018, KhulnaSoft, Ltd.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name KhulnaSoft, Ltd. nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL TECHEMPOWER, INC. BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/

package com.khulnasoft.security;

import java.math.*;
import java.nio.charset.*;
import java.security.*;

import com.khulnasoft.helper.*;

/**
 * SimpleDigester provides a simplified abstraction on the java.security
 * MessageDigest functionality. Internally it gets a new MessageDigest instance
 * for every call to avoid synchronization. Re-using the MessageDigest instance
 * only provided a modest speed increase that was deemed not worth the
 * additional locking that was encountered.
 *   <p>
 * The standard Sun Java Virtual Machine comes with MD5 and SHA algorithms.
 * If a different security provider were to be used (fairly unlikely), the
 * NoSuchAlgorithmException that is ignored below may be more important.  Be
 * aware that this class ignores that exception because it simply doesn't
 * ever come up with the standard VM.
 */
public class SimpleDigester
{

  //
  // Constants.
  //

  public static final int MD5_LENGTH = 32;
  public static final int HEXADECIMAL = 16;

  /**
   * Gets the MD5 Digester.
   */
  protected static MessageDigest getMd5Digester()
  {
    try
    {
      return MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException nsae)
    {
      return null;
    }
  }

  /**
   * Gets the SHA Digester.
   */
  protected static MessageDigest getShaDigester()
  {
    try
    {
      return MessageDigest.getInstance("SHA");
    }
    catch (NoSuchAlgorithmException nsae)
    {
      return null;
    }
  }

  /**
   * Digest a String using MD5.
   */
  public static String getMD5(String sourceString)
  {
    MessageDigest digest = getMd5Digester();
    byte[] results = digest.digest(sourceString.getBytes());
    digest.reset();
    return new String(results);
  }

  /**
   * Get the MD5 hash of a String in hex
   * @param source The String to hash
   * @return the hashed String
   */
  public static String getMD5Hex(String source) {
    byte[] bytesOfMessage = source.getBytes(StandardCharsets.UTF_8);
    MessageDigest digest = getMd5Digester();
    digest.reset();
    digest.update(bytesOfMessage);
    byte[] digestArray = digest.digest();      // Gets the hash and resets
    BigInteger bigInt = new BigInteger(1, digestArray);
    String hashText = bigInt.toString(HEXADECIMAL);
    return StringHelper.padZero(hashText, MD5_LENGTH); // Pad to 32 digits
  }

  /**
   * Digest a String using SHA.
   */
  public static String getSHA(String sourceString)
  {
    MessageDigest digest = getShaDigester();
    byte[] results = digest.digest(sourceString.getBytes());
    digest.reset();
    return new String(results);
  }

  /**
   * Digest a String using SHA, and return as a raw byte array.
   */
  public static byte[] getSHAAsByteArray(String sourceString)
  {
    MessageDigest digest = getShaDigester();
    byte[] results = digest.digest(sourceString.getBytes());
    digest.reset();    
    return results;
  }

  /**
   * Digest a String using SHA, and return as a hexadecimal String.
   */
  public static String getSHAHex(String sourceString)
  {
    byte[] bytesOfMessage = sourceString.getBytes(StandardCharsets.UTF_8);
    MessageDigest digest = getShaDigester();
    BigInteger result = new BigInteger(1, digest.digest(bytesOfMessage));
    digest.reset();
    return result.toString(HEXADECIMAL);  // 16 radix = hexadecimal
  }

  /**
   * Main tester method.
   */
  public static void main(String[] args)
  {
    System.out.println("Simple digester.  " + args.length + " arguments.");

    for (String arg : args)
    {
      System.out.println(arg
          + "  MD5: "
          + SimpleDigester.getMD5Hex(arg)
          + "  SHA: "
          + SimpleDigester.getSHAHex(arg));
    }
  }
}
