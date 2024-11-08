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
package com.khulnasoft.gemini.annotation.injector;

import java.lang.annotation.*;

/**
 * Injector is a meta annotation that allows a developer to specify their
 * custom annotation as being an Injector, the dispatcher can then recognize
 * the custom annotation. 
 * 
 * Injectors are used to specify how parameters of a handler method should be 
 * populated.
 * <pre>
 *  &#064;Injector(CustomInjector.class)
 *  &#064;Target(ElementType.PARAMETER) // tells the compiler that this annotation can only be used at the parameter level
 *  &#064;Retention(RetentionPolicy.RUNTIME)
 *  public &#064;interface MyInjector 
 *  {
 *    String value();
 *  }
 * </pre>
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Injector 
{
  /**
   * Returns the class responsible for extracting values of a given type from a
   * web request, for corresponding annotated request parameters.
   *
   * @return the class responsible for injecting values
   */
  @SuppressWarnings("rawtypes")
  Class<? extends ParameterInjector> value();
}
