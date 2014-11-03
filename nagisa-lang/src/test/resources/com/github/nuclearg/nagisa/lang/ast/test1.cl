let even=0
let odd=0

let i=1

while i<100
	if i%2 == 0 then
		even = even + i
	else
		odd = odd + i
	endif
wend

let sum = even + odd