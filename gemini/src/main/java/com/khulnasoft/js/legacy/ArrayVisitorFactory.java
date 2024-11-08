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

package com.khulnasoft.js.legacy;

import java.lang.reflect.*;

/**
 * Makes visitors for arrays.
 */
enum ArrayVisitorFactory implements VisitorFactory<Object>
{
  INSTANCE;

  @Override
  public Visitor visitor(Object object)
  {
    if (object == null || !object.getClass().isArray())
    {
      throw new IllegalArgumentException("Argument is not an array.");
    }
    return new ArrayVisitor(object);
  }

  /**
   * The Array Visitor implementation.
   */
  private static final class ArrayVisitor implements Visitor
  {
    private final Object array;
    private final int length;
    private int index = -1;

    private ArrayVisitor(Object array)
    {
      this.array = array;
      this.length = Array.getLength(array);
    }

    @Override
    public boolean hasNext()
    {
      return this.index < this.length - 1;
    }

    @Override
    public boolean isArray()
    {
      return true;
    }

    @Override
    public void next()
    {
      this.index++;
    }

    @Override
    public String name()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public Object value()
    {
      return Array.get(this.array, this.index);
    }
  }
}
