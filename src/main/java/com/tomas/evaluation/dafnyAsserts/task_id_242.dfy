method CountCharactersTest(){
  var res1:=CountCharacters("python programming");
  assert res1==18;

  var res2:=CountCharacters("language");
  assert res2==8;

  var res3:=CountCharacters("words");
  assert res3==5;
}