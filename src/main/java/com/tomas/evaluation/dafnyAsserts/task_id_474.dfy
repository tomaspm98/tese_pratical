method ReplaceCharsTest(){
  var out1:=ReplaceChars("polygon",'y','l');
  assert out1=="pollgon";

  var out2:=ReplaceChars("character",'c','a');
  assert out2=="aharaater";

  var out3:=ReplaceChars("python",'l','a');
  assert out3=="python";

}

