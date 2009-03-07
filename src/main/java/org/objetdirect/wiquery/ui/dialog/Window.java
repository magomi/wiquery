/*
 * Copyright (c) 2008 Objet Direct
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.objetdirect.wiquery.ui.dialog;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.objetdirect.wiquery.core.commons.WickextPlugin;
import org.objetdirect.wiquery.core.javascript.JsQuery;
import org.objetdirect.wiquery.core.javascript.JsStatement;
import org.objetdirect.wiquery.core.options.Options;
import org.objetdirect.wiquery.ui.commons.WickextUIPlugin;
import org.objetdirect.wiquery.ui.draggable.DraggableJavaScriptResourceLocator;
import org.objetdirect.wiquery.ui.resizable.ResizableJavaScriptResourceReference;

/**
 * $Id$
 * <p>
 * 	Displays a window wrapping this {@link WebMarkupContainer} markup.
 * </p>
 * <p>
 * 	This UI component is built from this {@link WebMarkupContainer}'s
 *  HTML markup.
 *  The correct markup should be a <code>div</code> HTML element
 *  wrapping the contents to display in this window.
 * </p>
 * <p>
 * 	Example:
 * 	<code>
 *  <pre>
 *   &lt;div wicket:id="id" title="The window title"&gt;
 *     The wrapped content
 *   &lt;/div&gt;
 *  </pre>
 *  </code>
 * </p>
 * 
 * @author Lionel Armanet
 * @since 0.5
 */
@WickextUIPlugin
public class Window extends WebMarkupContainer implements WickextPlugin {

	private static final long serialVersionUID = 1L;

	public static enum WindowPosition {
		TOP, BOTTOM, CENTER, LEFT, RIGHT
	};

	/**
	 * Some options can be set on a {@link Window}.
	 */
	private Options options;

	/**
	 * Builds a new instance of {@link Window} for the given wicket id.
	 * 
	 * @param id
	 *            the given wicket id.
	 */
	public Window(String id) {
		super(id);
		options = new Options();
		// default settings
		this.setAutoOpen(false);
		this.setPosition(WindowPosition.CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objetdirect.wickext.core.commons
	 *      .WickextPlugin#getHeaderContribution()
	 */
	public IHeaderContributor getHeaderContribution() {
		// imports this component's needed resources
		return new IHeaderContributor() {

			private static final long serialVersionUID = -6585283714994481625L;

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.apache.wicket.markup.html.IHeaderContributor#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
			 */
			public void renderHead(IHeaderResponse response) {
				// importing CSS theme and JavaScript needed resource to make
				// this component working properly
				response
						.renderJavascriptReference(new JavascriptResourceReference(
								Window.class, "ui.dialog.js"));
				// adding dependencies on draggble and resizable resources
				// in order to make the window draggable and resizable
				response
						.renderJavascriptReference(new DraggableJavaScriptResourceLocator());
				response
						.renderJavascriptReference(new ResizableJavaScriptResourceReference());
			}

		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objetdirect.wickext.core.commons.JavaScriptCallable#statement()
	 */
	public JsStatement statement() {
		return new JsQuery(this).$().chain("dialog", options.getJavaScriptOptions());
	}

	// TODO
	public JsStatement open() {
		return new JsQuery(this).$().chain("dialog", "'open'");
	}

	// TODO
	public JsStatement close() {
		return new JsQuery(this).$().chain("dialog", "'close'");
	}
	
	public void open(AjaxRequestTarget ajaxRequestTarget) {
		ajaxRequestTarget.appendJavascript(this.open().render().toString());
	}

	public void close(AjaxRequestTarget ajaxRequestTarget) {
		ajaxRequestTarget.appendJavascript(this.close().render().toString());
	}
	
	/**
	 * Sets if this window opens autmatically after the page is loaded.
	 * 
	 * @param autoOpen
	 *            true if the window auto opens, false otherwise
	 */
	public void setAutoOpen(boolean autoOpen) {
		options.put("autoOpen", autoOpen);
	}

	/**
	 * Sets if this window is modal or not.
	 * 
	 * @param modal
	 *            true if the window is modal, false otherwise
	 */
	public void setModal(boolean modal) {
		options.put("modal", modal);
	}

	/**
	 * Sets the overlay under the window. This parameter will take effect only
	 * if the {@link #setModal(boolean)} method is call with true.
	 * 
	 * @param ratio
	 *            a float value between 0 and 1 (1 is 100% black overlay)
	 */
	public void setOverlayRatio(float ratio) {
		// TODO nested options !
		options.put("overlay", "{opacity: " + ratio + ", background: 'black'}");
	}

	/**
	 * @return if this window auto opens on page loading.
	 */
	public boolean isAutoOpen() {
		return options.getBoolean("autoOpen");
	}

	/**
	 * @return if this window is modal.
	 */
	public boolean isModal() {
		return options.getBoolean("modal");
	}

	/**
	 * Sets the window's width.
	 */
	public void setWidth(int width) {
		options.put("width", width);
	}
	
	/**
	 * Returns the dialog's width.
	 */
	public int getWidth() {
		return options.getInt("width");
	}

	/**
	 * Sets the window's height.
	 */
	public void setHeight(int height) {
		options.put("height", height);
	}
	
	/**
	 * Returns the window's height.
	 */
	public int getHeight() {
		return options.getInt("height");
	}

	/**
	 * @deprecated
	 */
	public void positionTop() {
		options.put("position", "'top'");
	}

	/**
	 * Sets the window's position.
	 */
	public void setPosition(WindowPosition windowPosition) {
		options.putLiteral("position", windowPosition.name().toLowerCase());
	}
	
	/**
	 * Returns the {@link WindowPosition}.
	 */
	public WindowPosition getPosition() {
		String literal = options.getLiteral("position");
		return WindowPosition.valueOf(literal.toUpperCase());
	}

	/**
	 * Sets a css class to customize the window's display.
	 */
	public void setCssClass(String cssClass) {
		options.putLiteral("dialogClass", cssClass);
	}
	
	/**
	 * Returns the css class applied to customize this window.
	 */
	public String getCssClass() {
		return options.get("dialogClass");
	}
	
	/**
	 * Sets the effect used when the window closes.
	 * @param a {@link String} with the given effect's name.
	 */
	public void setHideEffect(String hideEffect) {
		options.putLiteral("hide", hideEffect);
	}
	
	/**
	 * Sets the effect used when the window shows itself.
	 * @param a {@link String} with the given effect's name.
	 */
	public void setShowEffect(String hideEffect) {
		options.putLiteral("show", hideEffect);
	}
	
	/**
	 * Sets the window's max height.
	 */
	public void setMaxHeight(int maxHeight) {
		options.put("maxHeight", maxHeight);
	}
	
	/**
	 * Returns the window's max height.
	 */
	public int getMaxHeight() {
		return options.getInt("maxHeight");
	}
	
	/**
	 * Sets the window's max width.
	 */
	public void setMaxWidth(int maxWidth) {
		options.put("maxWidth", maxWidth);
	}
	
	/**
	 * Returns the window's max width.
	 */
	public int getMaxWidth() {
		return options.getInt("maxWidth");
	}
	
	/**
	 * Sets the window's min height.
	 */
	public void setMinHeight(int minHeight) {
		options.put("minHeight", minHeight);
	}
	
	/**
	 * Returns the window's min height.
	 */
	public int getMinHeight() {
		return options.getInt("minHeight");
	}
	
	/**
	 * Sets the window's min width.
	 */
	public void setMinWidth(int minWidth) {
		options.put("minWidth", minWidth);
	}
	
	/**
	 * Returns the window's max width.
	 */
	public int getMinWidth() {
		return options.getInt("minWidth");
	}	
	
	/**
	 * Sets if this window is resizable or not.
	 */
	public void setResizable(boolean resizable) {
		options.put("resizable", resizable);
	}
	
	/**
	 * Returns <code>true</code> if this window is resizable.
	 */
	public boolean isResizable() {
		return options.getBoolean("resizable");
	}
	
	/**
	 * Sets the window's title.
	 * <p>
	 * 	<strong>Note:</strong> the title can be automatically sets when the
	 *  HTML <code>title</code> attribute is set.
	 * </p>
	 */
	public void setTitle(String title) {
		options.putLiteral("title", title);
	}
	
	/**
	 * Returns the window's title.
	 * @return a non null {@link String} containing the window's title.
	 */
	public String getTitle() {
		if (this.getTitle() != null && !"".equals(this.getTitle())) {
			return this.getTitle();
		}
		if (this.options.containsKey("title")) {
			return this.options.get("title");
		}
		return "";
	}

}
