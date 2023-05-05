# WeaponThrowLite
Adds the ability to throw all your weapons. As an addition you can enable throwing all the items.

The project is available here
https://www.curseforge.com/minecraft/mc-mods/weapon-throw

## Implementing interactions with other mods
### Warning: This part is not intented to be correct, just to be functional. 
To use the mod in modding enviroment, [cursemaven](https://www.cursemaven.com/) can be used. Go to build.gradle and something like this can be implemented
on the dependencies part.
```
compileOnly fg.deobf("curse.maven:weaponthrow-408332:3458711")
```
When the gradle project is refreshed it will download WeaponThrow 4.5 for 1.16.4/5 and can be referenced by the project. More information about syntax is founded on the cursemaven page.

Modders may evaluate the fabric equivalent of this statement:
```
ModList.get().isLoaded("weaponthrow")
```
Before every reference made into the code.



