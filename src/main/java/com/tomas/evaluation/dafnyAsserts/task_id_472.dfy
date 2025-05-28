method ContainsConsecutiveNumbersTest(){
  var a1:= new int[] [1,2,3,4,5];
  var out1:=ContainsConsecutiveNumbers(a1);
  expect out1==true;

  var a2:= new int[] [1,2,3,5,6];
  var out2:=ContainsConsecutiveNumbers(a2);
  expect out2==true;

  var a3:= new int[] [1,3,5];
  var out3:=ContainsConsecutiveNumbers(a3);
  expect out3==false;

}

