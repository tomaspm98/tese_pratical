method HasOppositeSignTest(){
  var out1:=HasOppositeSign(1,-2);
  assert out1==true;

  var out2:=HasOppositeSign(3,2);
  assert out2==false;
  
  var out3:=HasOppositeSign(-2,2);
  assert out3==true;
}

