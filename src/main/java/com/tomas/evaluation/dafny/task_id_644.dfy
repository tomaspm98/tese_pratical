method ReverseUptoK(s: array<int>, k: int)
    modifies s
    requires 2 <= k <= s.Length
    ensures forall i :: 0 <= i < k ==> s[i] == old(s[k - 1 - i])
    ensures forall i :: k <= i < s.Length ==> s[i] == old(s[i])
{
	var l := k - 1;
	var i := 0;
	while (i < l-i)
		invariant 0 <= i <= (l+1)/2;
		invariant forall p :: 0 <= p < i || l-i < p <= l ==> s[p] == old(s[l-p]);
		invariant forall p :: i <= p <= l-i ==> s[p] == old(s[p]);
        invariant forall p :: k <= p < s.Length ==> s[p] == old(s[p])
	{
		s[i], s[l-i] := s[l-i], s[i];
		i := i + 1;
	}

}