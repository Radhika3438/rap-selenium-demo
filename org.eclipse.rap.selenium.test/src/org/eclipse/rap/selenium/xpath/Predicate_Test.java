/*******************************************************************************
 * Copyright (c) 2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/

package org.eclipse.rap.selenium.xpath;

import static org.eclipse.rap.selenium.xpath.Predicate.not;
import static org.eclipse.rap.selenium.xpath.Predicate.with;
import static org.eclipse.rap.selenium.xpath.XPath.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class Predicate_Test {

  @Test
  public void testDefaultConstructor() {
    assertEquals( "", new Predicate().toString() );
  }

  @Test
  public void testFactory() {
    assertEquals( "", Predicate.with().toString() );
  }

  @Test
  public void testFactoryWithInitial() {
    assertEquals( "(foo)", with( "foo" ).toString() );
  }

  @Test
  public void testAnd() {
    assertEquals( "(foo) and (bar)", with( "foo" ).and( "bar" ).toString() );
  }

  @Test
  public void testOr() {
    assertEquals( "(foo) or (bar)", with( "foo" ).or( "bar" ).toString() );
  }

  @Test
  public void testWithSingleAttribute() {
    assertEquals( "@foo='bar'", Predicate.with().attr( "foo", "bar" ).toString() );
  }

  @Test
  public void testWithSingleAriaAttribute() {
    assertEquals( "@aria-foo='bar'", Predicate.with().aria( "foo", "bar" ).toString() );
  }

  @Test
  public void testWithSingleAttribute_nullKeyThrowsException() {
    try {
      with().attr( null, "bar" ).toString();
      fail();
    } catch( NullPointerException e ) {
      // expected
    }
  }

  @Test
  public void testWithSingleAttribute_EmptyStringKeyThrowsException() {
    try {
      with().attr( "", "bar" ).toString();
      fail();
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  @Test
  public void testWithSingleAttribute_nullValueThrowsException() {
    try {
      with().attr( "bar", null ).toString();
      fail();
    } catch( NullPointerException e ) {
      // expected
    }
  }
  @Test
  public void testWithMultipleAttributes() {
    String expected = "@foo='bar' and @foo2='bar2'";
    assertEquals( expected, Predicate.with().attr( "foo", "bar" ).attr( "foo2", "bar2" ).toString() );
  }

  @Test
  public void testNotWithAttribute() {
    assertEquals( "not(@foo='bar')", not( Predicate.with().attr( "foo", "bar" ) ).toString() );
  }

  @Test
  public void testNotWithAriaAttribute() {
    assertEquals( "not(@aria-foo='bar')", Predicate.with().notAria( "foo", "bar" ).toString() );
  }

  @Test
  public void testWithAttributesAndNotWithAttribute() {
    Predicate pred = Predicate.with().attr( "foo", "bar" ).notAttr( "foo2", "bar2" );

    String expected = "@foo='bar' and not(@foo2='bar2')";
    assertEquals( expected, pred.toString() );
  }

  @Test
  public void testWithAttributesAndStaticNotWithAttribute() {
    Predicate pred = Predicate.with().attr( "foo", "bar" ).and( not( Predicate.with().attr( "foo2", "bar2" ) ) );

    String expected = "@foo='bar' and (not(@foo2='bar2'))";
    assertEquals( expected, pred.toString() );
  }

  @Test
  public void testWithText() {
    assertEquals( "text()='bar'", with().text( "bar" ).toString() );
  }

  @Test
  public void testWithId() {
    assertEquals( "@id='bar'", with().id( "bar" ).toString() );
  }

  @Test
  public void testWithPosition() {
    assertEquals( "(count(preceding-sibling::*)+1)=3", with().offset( 3 ).toString() );
  }

  @Test
  public void testWithPositionMinusOne() {
    assertEquals( "(count(following-sibling::*)+1)=1", with().offset( -1 ).toString() );
  }

  @Test
  public void testWithPositionZero() {
    try {
      with().offset( 0 );
      fail();
    } catch( IllegalArgumentException e) {

    }
  }

  @Test
  public void testWithId_EmptyStringParameterThrowsException() {
    try {
      with().id( "" );
      fail();
    } catch( IllegalArgumentException e ) {
      // expected
    }
  }

  @Test
  public void testWithString() {
    assertEquals( "contains(text(),'bar')", with().string( "bar" ).toString() );
  }

  @Test
  public void testWithContent() {
    assertEquals( "count(.//a)>0", with().content( any().element( "a" ) ).toString() );
  }

  @Test
  public void testWithTextContent() {
    String expected = "count(descendant-or-self::*[text()='foo'])>0";
    assertEquals( expected, with().textContent( "foo" ).toString() );
  }

  @Test
  public void testWithStringContent() {
    String expected = "count(descendant-or-self::*[contains(text(),'foo')])>0";
    assertEquals( expected, with().stringContent( "foo" ).toString() );
  }

}
