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
package com.khulnasoft.collection;

import java.util.*;

import com.khulnasoft.helper.*;

/**
 * An implementation of MutableNamedValues that uses a backing map containing
 * Objects, adding getObject and putObject methods.
 */
public class   MutableNamedObjects
    implements MutableNamedValues
{
  
  private Map<String, Object> map;
  
  /**
   * Constructor.
   */
  public MutableNamedObjects()
  {
  }
  
  /**
   * Lazy initialization.
   */
  private void lazyInitialize()
  {
    if (map == null)
    {
      map = new HashMap<>();
    }
  }
  
  /**
   * Gets a copy of the backing map.
   */
  public Map<String, Object> asMap()
  {
    if (map == null)
    {
      return Collections.emptyMap();
    }
    else
    {
      return new HashMap<>(map);
    }
  }
  
  /**
   * Copy all elements to the supplied Map.
   */
  public MutableNamedObjects copyTo(Map<String, Object> destination)
  {
    if (map != null)
    {
      destination.putAll(map);
    }
    return this;
  }
  
  /**
   * Returns the number of elements in the backing map.
   */
  public int size()
  {
    return map != null ? map.size() : 0;
  }
  
  /**
   * Get an arbitrary object.
   */
  @SuppressWarnings("unchecked")
  public <O extends Object> O getObject(String name, O defaultValue)
  {
    final O obj = map != null ? (O)map.get(name) : null;
    return (obj != null)
        ? obj
        : defaultValue;
  }

  /**
   * Get an arbitrary object.  Returns null if the named value is not present.
   */
  public <O extends Object> O getObject(String name)
  {
    return getObject(name, null);
  }

  /**
   * Store an arbitrary object.
   */
  public MutableNamedObjects putObject(String name, Object value)
  {
    lazyInitialize();
    map.put(name, value);
    return this;
  }

  @Override
  public boolean has(String name)
  {
    return map != null ? map.containsKey(name) : false;
  }

  @Override
  public Set<String> names()
  {
    if (map != null)
    {
      return map.keySet();
    }
    else
    {
      return Collections.emptySet();
    }
  }

  @Override
  public String get(String name)
  {
    return get(name, null);
  }

  @Override
  public String get(String name, String defaultValue)
  {
    try
    {
      final String value = map != null ? (String)map.get(name) : null;
      if (value != null)
      {
        return value;
      }
    }
    catch (ClassCastException ccexc)
    {
      // Do nothing.
    }
    
    return defaultValue;
  }

  @Override
  public int getInt(String name)
  {
    return getInt(name, 0);
  }

  @Override
  public int getInt(String name, int defaultValue)
  {
    try
    {
      final Integer value = map != null ? (Integer)map.get(name) : null;
      if (value != null)
      {
        return value;
      }
    }
    catch (ClassCastException ccexc)
    {
      // Do nothing.
    }
    
    return defaultValue;
  }

  @Override
  public int getInt(String name, int defaultValue, int minimum, int maximum)
  {
    return NumberHelper.boundInteger(getInt(name, defaultValue), minimum, maximum);
  }

  @Override
  public long getLong(String name, long defaultValue, long minimum,
      long maximum)
  {
    return NumberHelper.boundLong(getLong(name, defaultValue), minimum, maximum);
  }

  @Override
  public long getLong(String name)
  {
    return getLong(name, 0L);
  }

  @Override
  public long getLong(String name, long defaultValue)
  {
    try
    {
      final Long value = map != null ? (Long)map.get(name) : null;
      if (value != null)
      {
        return value;
      }
    }
    catch (ClassCastException ccexc)
    {
      // Do nothing.
    }
    
    return defaultValue;
  }

  @Override
  public boolean getBoolean(String name)
  {
    return getBoolean(name, false);
  }

  @Override
  public boolean getBoolean(String name, boolean defaultValue)
  {
    try
    {
      final Boolean value = map != null ? (Boolean)map.get(name) : null;
      if (value != null)
      {
        return value;
      }
    }
    catch (ClassCastException ccexc)
    {
      // Do nothing.
    }
    
    return defaultValue;
  }

  @Override
  public MutableNamedObjects put(String name, String value)
  {
    return putObject(name, value);
  }

  @Override
  public MutableNamedObjects put(String name, int value)
  {
    return putObject(name, value);
  }

  @Override
  public MutableNamedObjects put(String name, long value)
  {
    return putObject(name, value);
  }

  @Override
  public MutableNamedObjects put(String name, boolean value)
  {
    return putObject(name, value);
  }

  @Override
  public MutableNamedObjects remove(String name)
  {
    if (map != null)
    {
      map.remove(name);
    }
    return this;
  }

  @Override
  public MutableNamedObjects clear()
  {
    if (map != null)
    {
      map.clear();
    }
    return this;
  }

}
