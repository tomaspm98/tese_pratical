method ContainsZTest(){
  var out1:=ContainsZ("pythonz");
  expect out1==true;

  var out2:=ContainsZ("xyz.");
  expect out2==true;

  var out3:=ContainsZ("  lang  .");
  expect out3==false;

}
