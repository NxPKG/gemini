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

import java.util.*;

/**
 * Makes visitors for iterators.
 */
enum IteratorVisitorFactory implements VisitorFactory<Object>
{
  INSTANCE;

  @Override
  public Visitor visitor(Object object)
  {
    if (!(object instanceof Iterator))
    {
      throw new IllegalArgumentException("Argument is not an iterator.");
    }
    return new IteratorVisitor<>((Iterator<?>) object);
  }

  /**
   * The Iterator Visitor implementation.
   */
  static final class IteratorVisitor<E> implements Visitor
  {
    private final Iterator<E> iterator;
    private E value;

    IteratorVisitor(Iterator<E> iterator)
    {
      this.iterator = iterator;
    }

    @Override
    public boolean hasNext()
    {
      return this.iterator.hasNext();
    }

    @Override
    public boolean isArray()
    {
      return true;
    }

    @Override
    public String name()
    {
      throw new UnsupportedOperationException();
    }

    @Override
    public void next()
    {
      this.value = this.iterator.next();
    }

    @Override
    public Object value()
    {
      return this.value;
    }
  }
}
