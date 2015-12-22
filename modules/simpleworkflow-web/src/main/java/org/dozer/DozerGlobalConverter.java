package org.dozer;


public abstract class DozerGlobalConverter<A, B> extends DozerConverter<A, B> {

  private Class<A> prototypeA;
  private Class<B> prototypeB;

  public DozerGlobalConverter(Class<A> prototypeA, Class<B> prototypeB) {
    super(prototypeA, prototypeB);
   this.prototypeA=prototypeA;
   this.prototypeB=prototypeB;
  }

  
  public Class<A> getPrototypeA() {
    return prototypeA;
  }

  
  public Class<B> getPrototypeB() {
    return prototypeB;
  }

}
