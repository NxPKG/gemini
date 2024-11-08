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

package com.khulnasoft.data;

import java.sql.*;

import com.khulnasoft.data.jdbc.*;
import com.khulnasoft.util.*;

/**
 * A special implementation of the DatabaseConnectorListener interface that
 * re-broadcasts to a list of implementations.  This was added when adding
 * the new com.khulnasoft.gemini.monitor package so that a second database
 * connection listener could be notified of database connectivity events.
 */
public class DatabaseConnectionListenerList 
  implements DatabaseConnectionListener
{

  //
  // Variables.
  //
  
  private final DatabaseConnectionListener[] listeners;
  
  //
  // Methods.
  //
  
  /**
   * Constructor.
   */
  public DatabaseConnectionListenerList(DatabaseConnectionListener... listeners)
  {
    this.listeners = listeners.clone();
  }

  /**
   * Pass-through to each of the listeners in the list.
   */
  @Override
  public int exceptionInExecuteBatch(SQLException exc, JdbcConnector conn)
  {
    int toReturn = INSTRUCT_DO_NOTHING;
    for (DatabaseConnectionListener listener : this.listeners)
    {
      toReturn = Math.max(toReturn,
          listener.exceptionInExecuteBatch(exc, conn));
    }
    return toReturn;
  }

  /**
   * Pass-through to each of the listeners in the list.
   */
  @Override
  public int exceptionInRunQuery(SQLException exc, JdbcConnector conn)
  {
    int toReturn = INSTRUCT_DO_NOTHING;
    for (DatabaseConnectionListener listener : this.listeners)
    {
      toReturn = Math.max(toReturn, listener.exceptionInRunQuery(exc, conn));
    }
    return toReturn;
  }

  /**
   * Pass-through to each of the listeners in the list.
   */
  @Override
  public int exceptionInRunUpdateQuery(SQLException exc, JdbcConnector conn)
  {
    int toReturn = INSTRUCT_DO_NOTHING;
    for (DatabaseConnectionListener listener : this.listeners)
    {
      toReturn = Math.max(toReturn,
          listener.exceptionInRunUpdateQuery(exc, conn));
    }
    return toReturn;
  }

  /**
   * Pass-through to each of the listeners in the list.
   */
  @Override
  public void queryStarting()
  {
    for (DatabaseConnectionListener listener : this.listeners)
    {
      listener.queryStarting();
    }
  }

  /**
   * Pass-through to each of the listeners in the list.
   */
  @Override
  public void queryCompleting()
  {
    for (DatabaseConnectionListener listener : this.listeners)
    {
      listener.queryCompleting();
    }
  }

  /**
   * Pass-through to each of the listeners in the list.
   */
  @Override
  public void configure(EnhancedProperties props)
  {
    for (DatabaseConnectionListener listener : this.listeners)
    {
      listener.configure(props);
    }
  }

}
