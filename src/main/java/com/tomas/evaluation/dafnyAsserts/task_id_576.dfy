method IsSublistTest(){
  var a1:seq<int>:= [1,4,3,5];
  var a2:seq<int>:= [1,2];
  var out1:=IsSublist(a2,a1);
  expect out1==false;

  var a3:seq<int>:=  [1,2,1];
  var a4:seq<int>:=  [1,2,1];
  var out2:=IsSublist(a4,a3);
  expect out2==false;

  var a5:seq<int>:=  [1,0,2,2];
  var a6:seq<int>:=  [0,2,2];
  var out3:=IsSublist(a6,a5);
  expect out3==false;

}

