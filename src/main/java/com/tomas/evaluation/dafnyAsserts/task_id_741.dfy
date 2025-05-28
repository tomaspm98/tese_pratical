method AllCharactersSameTest(){
  var out1:=AllCharactersSame("python");
  expect out1==false;

  var out2:=AllCharactersSame("aaa");
  expect out2==true;

  var out3:=AllCharactersSame("data");
  expect out3==false;
  
}
