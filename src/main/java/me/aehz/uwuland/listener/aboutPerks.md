<h1>About Perks and Events</h1>
<h2> Design philosophy behind the three type system </h2>
There are 3 types of perks. They are categorized by their maximum scope.
<h3> Perk:  </h3>
Standard perks can only be applied to singular entities. This does not mean that they can't affect their surroundings. 

Standard perks should never be fully negative.
Fully negative perks should instead be Group Perks to encourage applying them to multiple people.
Shared suffering is more likely to still result in a positive player experience.

<h3> Group Perk: </h3>
Group perks can be applied to a group of entities. 
They can still be applied to singular entities by using a list containing only one entry. 
However, they may not have an effect in those cases (E.g. shuffling the location of a single player).

They can technically be applied on a global scale, but this should be avoided, in order to avoid technical issues and
guarantee a positive player experience.

<h3> Global Event: </h3>
Global events can safely be applied to all entities.

Global events also include changes to the game that do not require an entity to function (E.g. making wood unbreakable).
This is also why they are not called perks.

<h2>Technical details </h2>

<h3> Perk: </h3>

Despite only ever being applied to a single entity at a time,
standard perks still require this entity to be inside a mutableList of length 1.
This may seem counterintuitive at first, but it allows for a consistent experience when dealing with all perk types.

<h3> Group Perk: </h3>

TODO

<h3> Global Event: </h3>

Global events are virtually the same as Group Perks and can be applied in the same way Group Perks are.
The only difference between them is their ability to be enabled globally.


<h2> Timed Perks: </h2>

Timed perks are not a type of perk in the sense that the three types are. They do not have a game design philosophy.
Any perk that requires a recurring action to take place is a timed perk.

They can easily be implemented by:

1. Extending the TimedPerk interface in addition to the appropriate PerkListener interface.
2. Defining a task in the `task()` function
3. Defining a delay using `stg["min"]` and `stg["max"]`
4. Executing `startTask(targets)` in `setup()`