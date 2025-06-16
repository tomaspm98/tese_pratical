method IsNonPrimeTest(){

  var res1:=IsNonPrime(2);
  expect res1==false;

  var res2:=IsNonPrime(10);
  expect res2==true;

  var res3:=IsNonPrime(35);
  expect res3==true;

}