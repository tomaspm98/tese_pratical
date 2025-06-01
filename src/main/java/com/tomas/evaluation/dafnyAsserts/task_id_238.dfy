method CountNonEmptySubstringsTest(){
  var res1:=CountNonEmptySubstrings("abc");
  assert res1==6;

  var res2:=CountNonEmptySubstrings("abcd");
  assert res2==10;

  var res3:=CountNonEmptySubstrings("abcde");
  assert res3==15;

}