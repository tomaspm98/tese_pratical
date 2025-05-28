method IsPerfectSquareTest(){

  var out1:=IsPerfectSquare(10);
  expect out1==false;

  var out2:=IsPerfectSquare(36);
  expect out2==true;

  var out3:=IsPerfectSquare(14);
  expect out3==false;

}
