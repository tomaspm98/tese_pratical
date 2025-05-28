method ToUppercaseTest(){
  var out1:=ToUppercase("person");
  assert out1=="PERSON";

  var out2:=ToUppercase("final");
  assert out2=="FINAL";

  var out3:=ToUppercase("Valid");
  assert out3=="VALID";

}
