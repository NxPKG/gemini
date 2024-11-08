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
package com.khulnasoft.gemini.jsp;

import java.util.*;

import com.khulnasoft.*;
import com.khulnasoft.gemini.*;
import com.khulnasoft.helper.*;

/**
 * Maintains and renders a list of (Java)Scripts and (Style)Sheets for use
 * on a web page.  A typical web application will use multiple instances
 * of ScriptsAndSheets objects at three different scopes.  Specifically:
 * 
 * <ul>
 * <li>Application scope, as managed by the Infrastructure.  Here an 
 *     application may maintain references to scripts and style-sheets that 
 *     are expected to be used on -all- views generated by the application.
 *     An example may be the jQuery JavaScript library and a core set of
 *     application JavaScript.</li>
 * <li>Page scope, as managed by the application's Jsp subclass (that is, one
 *     instance for each JSP file).  Here, an application would reference
 *     scripts that are relevant to the site section (e.g., "Forum") or the
 *     specific JSP ("Forum Thread View").  An example may be forum.js
 *     or forum.css.</li>
 * <li>Request scope, as managed by the RequestVariables object provided by
 *     the standard include-variables.jsp file.  The request scope is the
 *     least-likely to be used as only in very rare cases do the scripts
 *     or style-sheets used by a JSP differ based on the particulars of a
 *     request.  But in theory, you could add the jQuery Float library if the
 *     user requested data to be rendered in graphical view where a default
 *     is tabular view.</li>
 * </ul>
 * 
 * In addition to scripts and style-sheets being referenced in these three
 * scopes, ScriptsAndSheets also maintains separate lists for Development,
 * Test, and Production environments.  Typically, scripts in use within 
 * Development are raw human-readable JavaScript; Test and Production then use
 * minified and possibly unified script files.  The standard addScript method
 * takes two parameters: the name of a script file for use in Development
 * and another for use in Test/Production.  A typical use would then be:
 * 
 * <pre>
 * sas.addScript("forum.js", "forum-min.js");
 * sas.addScript("forum-view.js", "forum-min.js");
 * sas.addScript("forum-edit.js", "forum-min.js");
 * </pre>
 * 
 * It is also possible to omit a filename for one or more of the deployment
 * environments.  This would be common if, for example, you minified all
 * of your application's JavaScript files into "app-min.js" and didn't want
 * the rendering of page-scope scripts to repeat a reference to app-min.js
 * after application-scope scripts had already rendered such a reference.
 * 
 * <pre>
 * sas.addScript("page-specific.js", "");  // Expect minified JS file at application-scope.
 * </pre>
 * 
 * In Development, sas.render() would render three Script tags; whereas in
 * Test and Production, sas.render() would render only one Script tag (for
 * forum-min.js).
 *   <p>
 * Note that this class assumes that the deployment environment configuration
 * of an application will not change during runtime.  Such a thing would be
 * exceedingly rare and probably will never happen.  But if you were to
 * change the environment at runtime, the computed sheets and scripts would
 * not change.
 */
public class ScriptsAndSheets
{

  //
  // Constants.
  //
  
  private static final int DEFAULT_LIST_LENGTH = 4;
  
  //
  // Member variables.
  //
  
  private int          environment = Version.ENV_DEVELOPMENT;
  
  private List<String> scripts = null;
  private List<String> suppressedScripts = null;
  private List<Sheet>  sheets = null;
  private List<String> suppressedSheets = null;

  private String       favIcon = null;
  
  //
  // Member methods.
  //
  
  /**
   * Constructor.
   * 
   * @param application a reference to your Application so that the deployment
   *        environment can be ascertained (by examining the Version object).
   */
  public ScriptsAndSheets(GeminiApplication application)
  {
    if (application != null)
    {
      this.environment = application.getVersion().getEnvironment();
    }
    
    // Assume Development environment if application reference is null.
  }
  
  /**
   * Add a Script (a reference to an external JavaScript file).  An empty
   * filename will be ignored.
   * 
   * @param development Script filename in Development environment.
   * @param test Script filename in Test environment.
   * @param production Script filename in Production environment.
   */
  public void addScript(String development, String test, String production)
  {
    if (this.scripts == null)
    {
      this.scripts = new ArrayList<>(DEFAULT_LIST_LENGTH);
    }

    String toAdd = null;
    
    switch (this.environment)
    {
      case (Version.ENV_DEVELOPMENT) :
      {
        if (StringHelper.isNonEmpty(development))
        {
          toAdd = development;
        }
        break;
      }
      case (Version.ENV_TEST) :
      {
        if (StringHelper.isNonEmpty(test))
        {
          toAdd = test;
        }
        break;
      }
      case (Version.ENV_PRODUCTION) :
      default:
      {
        if (StringHelper.isNonEmpty(production))
        {
          toAdd = production;
        }
        break;
      }
    }
    
    if (  (toAdd != null)
       && (!this.scripts.contains(toAdd))
       )
    {
      this.scripts.add(toAdd);
    }
  }
  
  /**
   * Add a Script (a reference to an external JavaScript file).
   * 
   * @param development Script filename in Development environment.
   * @param testAndProduction Script filename in Test and Production 
   *        environments.
   */
  public void addScript(String development, String testAndProduction)
  {
    addScript(development, testAndProduction, testAndProduction);
  }
  
  /**
   * Add a Script (a reference to an external JavaScript file).
   * 
   * @param allEnvironments Script filename for use in all environments.
   */
  public void addScript(String allEnvironments)
  {
    addScript(allEnvironments, allEnvironments, allEnvironments);
  }
  
  /**
   * Add a Script to the suppression list.  This is for the rare circumstances
   * where you want to override a script provided by default by a more general
   * scope.  For example, if the application always includes jQuery but on
   * one page, you want to omit jQuery, add jquery.js to the suppression list
   * for the page scope.  Calls to InfrastructureJsp.renderScripts() will then
   * pass the suppression list to the application-scope SAS object and remove
   * jQuery for that page.
   */
  public void suppressScript(String allEnvironments)
  {
    if (this.suppressedScripts == null)
    {
      this.suppressedScripts = new ArrayList<>(DEFAULT_LIST_LENGTH);
    }
    
    this.suppressedScripts.add(allEnvironments);
  }
  
  /**
   * Adds any suppressed scripts to the provided List.  If the provided List
   * is null, a new ArrayList is created with the suppressed scripts.  If
   * there are no suppressed scripts, the parameter is returned unchanged.
   */
  public List<String> getSuppressed(List<String> listToBuildUpon)
  {
  	List<String> toRet = listToBuildUpon;
    if (this.suppressedScripts != null)
    {
      if (toRet == null)
      {
      	toRet = new ArrayList<>(this.suppressedScripts);
      }
      else
      {
      	toRet.addAll(this.suppressedScripts);
      }
    }
      
    return toRet;
  }
  
  /**
   * Add a Style-sheet (a reference to an external CSS file).  An empty
   * filename will be ignored.
   * 
   * @param media An optional media attribute (e.g., "screen" or "print") to
   *   include as part of the rendered link tag; null will result in no
   *   media attribute being rendered into the link tag. 
   * @param development Sheet filename in Development environment.
   * @param test Sheet filename in Test environment.
   * @param production Sheet filename in Production environment.
   */
  public void addSheet(String media, String development, String test, String production)
  {
    if (this.sheets == null)
    {
      this.sheets = new ArrayList<>(DEFAULT_LIST_LENGTH);
    }

    Sheet toAdd = null;

    switch (this.environment)
    {
      case (Version.ENV_DEVELOPMENT) :
      {
        if (StringHelper.isNonEmpty(development))
        {
          toAdd = new Sheet(media, development);
        }
        break;
      }
      case (Version.ENV_TEST) :
      {
        if (StringHelper.isNonEmpty(test))
        {
          toAdd = new Sheet(media, test);
        }
        break;
      }
      case (Version.ENV_PRODUCTION) :
      default:
      {
        if (StringHelper.isNonEmpty(production))
        {
          toAdd = new Sheet(media, production);
        }
        break;
      }
    }
    
    if (  (toAdd != null)
       && (!this.sheets.contains(toAdd))
       )
    {
      this.sheets.add(toAdd);
    }
  }
  
  /**
   * Add a Style-sheet (a reference to an external CSS file).  This version
   * assumes that the media attribute is not being defined (and will therefore
   * not get rendered into the link tag).
   * 
   * @param development Sheet filename in Development environment.
   * @param test Sheet filename in Test environment.
   * @param production Sheet filename in Production environment.
   */
  public void addSheet(String development, String test, String production)
  {
    addSheet(null, development, test, production);
  }
  
  /**
   * Add a Style-sheet (a reference to an external CSS file).
   * 
   * @param development Sheet filename in Development environment.
   * @param testAndProduction Sheet filename in Test and Production 
   *        environments.
   */
  public void addSheet(String development, String testAndProduction)
  {
    addSheet(null, development, testAndProduction, testAndProduction);
  }
  
  /**
   * Add a Style-sheet (a reference to an external CSS file).
   * 
   * @param allEnvironments Sheet filename for use in all environments.
   */
  public void addSheet(String allEnvironments)
  {
    addSheet(null, allEnvironments, allEnvironments, allEnvironments);
  }

  /**
   * Add a Sheet to the suppression list.  This is for the rare circumstances
   * where you want to exclude a sheet on a particular page.
   */
  public void suppressSheet(String allEnvironments)
  {
    if (this.suppressedSheets == null)
    {
      this.suppressedSheets = new ArrayList<>(DEFAULT_LIST_LENGTH);
    }
    this.suppressedSheets.add(allEnvironments);
  }

  /**
   * Adds any suppressed sheets to the provided List.  If the provided List
   * is null, a new ArrayList is created with the suppressed sheets.  If
   * there are no suppressed sheets, the parameter is returned unchanged.
   */
  public List<String> getSuppressedSheets(List<String> listToBuildUpon)
  {
  	List<String> toRet = listToBuildUpon;
    if (this.suppressedSheets != null)
    {
      if (toRet == null)
      {
      	toRet = new ArrayList<>(this.suppressedSheets);
      }
      else
      {
      	toRet.addAll(this.suppressedSheets);
      }
    }
    return toRet;
  }

  /**
   * Sets the favicon, which is assumed to be stored within the image 
   * directory.  The parameter here should be a relative path from the
   * image directory.  E.g., setFavicon("icons/main.png");
   * 
   * @param favIcon a relative path (from the deployment's image directory)
   *   to the favicon to use in a link rel="shortcut icon" tag.
   */
  public void setFavicon(String favIcon)
  {
    this.favIcon = favIcon;
  }
  
  /**
   * Renders the favIcon's link rel="shortcut icon" tag.  Returns an empty
   * string if no favicon has been specified in this particular 
   * ScriptsAndSheets.  Typically this is called by BasicJsp.renderFavicon()
   * so that the order of precedence for icons is request &gt; page &gt; app.
   */
  public String renderFavicon(Context context)
  {
    if (this.favIcon == null)
    {
      // No favicon has been defined.
      return "";
    }

    // We can't do a one-time rendering because the Image directory can
    // vary based on insecure/secure state.
    /*
    if (this.renderedFavicon == null)
    {
      if (this.favIcon != null)
      {
        this.renderedFavicon = "<link rel=\"SHORTCUT ICON\" href=\""
          + context.getInfrastructure().getImageURL(this.favIcon, context)
          + "\" />";
      }
      else
      {
        this.renderedFavicon = "";
      }
    }
    
    return this.renderedFavicon;
    */

    return "<link rel=\"SHORTCUT ICON\" href=\""
      + context.getInfrastructure().getImageUrl(this.favIcon, context)
      + "\" />";
  }
  
  /**
   * Gets a rendering of script tags.
   * 
   * @param context the request context
   * @param suppressed an optional list of suppressed script names
   */
  public String renderScripts(Context context, List<String> suppressed)
  {
    if (this.scripts == null)
    {
      // No scripts have been defined.
      return "";
    }

    StringBuilder sb = new StringBuilder();
    for (String script : this.scripts)
    {
      if (  (suppressed == null)
         || (!suppressed.contains(script))
         )
      {
        sb.append("<script src=\"");
        sb.append(NetworkHelper.render(
            context.getInfrastructure().getJavaScriptDirectory(context) + script));
        sb.append("\"></script>");
      }
    }
    return sb.toString();
  }
  
  /**
   * Gets a rendering of script tags.
   */
  public String renderScripts(Context context)
  {
    return renderScripts(context, null);
  }
  
  /**
   * Gets a rendering of style-sheet tags.
   *
   * @param context the request context
   * @param suppressed an optional list of suppressed sheet names
   */
  public String renderSheets(Context context, List<String> suppressed)
  {
    if (this.sheets == null)
    {
      // No sheets have been defined.
      return "";
    }

    StringBuilder sb = new StringBuilder();
    for (Sheet sheet : this.sheets)
    {
      if ((suppressed == null) || (!suppressed.contains(sheet.name)))
      {
        sb.append("<link rel=\"stylesheet");
        if (StringHelper.endsWithIgnoreCase(sheet.name, ".less"))
        {
          sb.append("/less");
        }
        sb.append("\" ");
        if (sheet.media != null)
        {
          sb.append("media=\"")
            .append(sheet.media)
            .append("\" ");
        }
        sb.append("href=\"");
        sb.append(NetworkHelper.render(
            context.getInfrastructure().getCssDirectory(context) + sheet.name));
        sb.append("\" />");
      }
    }
    return sb.toString();
  }

  /**
   * Gets a rendering of style-sheet tags.
   */
  public String renderSheets(Context context)
  {
    return renderSheets(context, null);
  }
  
  /**
   * A structure representing a style-sheet name and its media ("screen",
   * "print", etc.)
   */
  public static class Sheet
  {
    private String media;
    private String name;
    
    public Sheet(String media, String name)
    {
      this.media = media;
      this.name = name;
    }

    @Override
    public boolean equals(Object obj)
    {
      return (obj instanceof Sheet)
          && this.name.equals(((Sheet)obj).name);
    }

    @Override
    public int hashCode()
    {
      return this.name.hashCode();
    }
  }
  
}
