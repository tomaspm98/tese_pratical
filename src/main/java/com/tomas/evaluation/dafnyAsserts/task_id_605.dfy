method IsPrimeTest(){
  var out1:=IsPrime(13);
  expect out1==true;

  var out2:=IsPrime(7);
  expect out2==true;

  var out3:=IsPrime(1010);
  expect out3==false;

}

