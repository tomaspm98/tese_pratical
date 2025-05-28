method CountDigitsTest(){

  var out1:=CountDigits("program2bedone");
  expect out1==1;

  var out2:=CountDigits("3wonders");
  expect out2==1;

  var out3:=CountDigits("3wond-1ers2");
  expect out3==3;

}

