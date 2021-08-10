# WeaponThrow
Adds the ability to throw all your weapons. As an addition you can enable throwing all the items.

The project is available here
https://www.curseforge.com/minecraft/mc-mods/weapon-throw

## Implementing interactions with other mods
### Warning: This part is not intented to be correct, just to be functional. 
To use the mod in modding enviroment, [cursemaven](https://www.cursemaven.com/) can be used. Go to build.gradle and something like this can be implemented
on the dependencies part.
```
compileOnly fg.deobf("curse.maven:weaponthrow-408332:3421020")
```
When the gradle project is refreshed it will download WeaponThrow 4.4 for 1.16.4 and can be referenced by the project. More information about syntax is founded on the cursemaven page.

Modders may evaluate:
```
ModList.get().isLoaded("weaponthrow")
```
Before every reference made into the code.

## On-board forge events.
Inside there are 3 events that could help implementing the mod on other mods.

WeaponThrowEvent.TestThrow: Can be used to test the throwability of such weapon or item.

WeaponThrowEvent.OnThrow: Can be used to change the damage, velocity and exhaustion taken by player. 

WeaponThrowEvent.OnImpact: Can be used to modify the impact with an entity or a block.


