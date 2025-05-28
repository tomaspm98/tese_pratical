method FindFirstOccurrenceTest(){

  var a1:= new int[] [2, 5, 5, 5, 6, 6, 8, 9, 9, 9];
  var out1:=FindFirstOccurrence(a1,5);
  expect out1==1;

  var a2:= new int[] [2, 3, 5, 5, 6, 6, 8, 9, 9, 9];
  var out2:=FindFirstOccurrence(a2,5);
  expect out2==2;

  var a3:= new int[] [2, 4, 1, 5, 6, 6, 8, 9, 9, 9];
  var out3:=FindFirstOccurrence(a3,6);
  expect out3==4;

}

