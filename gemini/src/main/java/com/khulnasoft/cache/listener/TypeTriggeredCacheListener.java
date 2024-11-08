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
package com.khulnasoft.cache.listener;

import com.khulnasoft.cache.*;
import com.khulnasoft.util.*;

/**
 * Monitors cache expiration events and calls the abstract "expired" method
 * when any cache events occur with respect to a specific cached class.
 */
public abstract class TypeTriggeredCacheListener<I extends Identifiable> 
    implements CacheListener
{
  
  private final Class<I> watchedType;
  
  /**
   * Constructor.
   */
  public TypeTriggeredCacheListener(Class<I> type)
  {
    this.watchedType = type;
  }
  
  /**
   * The watched type has been expired.
   */
  public abstract void expired();

  @Override
  public void cacheFullReset()
  {
    // A full reset includes the watchedType.
    expired();
  }

  @Override
  public <T extends Identifiable> void cacheTypeReset(Class<T> type)
  {
    if (type == watchedType)
    {
      expired();
    }
  }

  @Override
  public <T extends Identifiable> void cacheObjectExpired(Class<T> type,
      long identity)
  {
    if (type == watchedType)
    {
      expired();
    }
  }

  @Override
  public <T extends Identifiable> void removeFromCache(Class<T> type,
      long identity)
  {
    if (type == watchedType)
    {
      expired();
    }
  }

}
