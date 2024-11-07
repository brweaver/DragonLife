// taken from: https://2dengine.com/doc/platformers.html

// what is the gravity that would allow jumping to a given height?
val jumpHeight = 5 // unit in coordinates of the game
var timeToApex = 0.44 // in seconds
//val gravity = 9.8 // (2*jumpHeight)/(timeToApex^2)
val gravity = (2*jumpHeight)/(timeToApex*timeToApex)
// what is the initial jump velocity?
val initJumpVelocity = math.sqrt(2*gravity*jumpHeight)

// how long does it take to reach the maximum height of a jump?
// note: if "initJumpVelocity" is not a multiple of "g" the maximum height is reached between frames
timeToApex = initJumpVelocity/gravity

Console.println(s"Gravity: $gravity")
Console.println(s"Initial Jump Velocity: $initJumpVelocity")

