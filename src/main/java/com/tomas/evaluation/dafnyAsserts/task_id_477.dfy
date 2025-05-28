method ToLowercaseTest(){
  var out1:=ToLowercase("InValid");
  assert out1=="invalid";

  var out2:=ToLowercase("TruE");
  assert out2=="true";

  var out3:=ToLowercase("SenTenCE");
  assert out3=="sentence";
  
}

