Trades
======

Trades plugin for @Electroid

## Compiling
`mvn clean package`

## Basic XML

  <root>
    <looksfor block="glass">
  	  <lookfor location="1,2,3" />
  	  <lookfor location="2,4,6" />
    </looksfor>

    <lookfor block="bedrock" location="0,0,0" /> <!-- Without this, the trade command will not work -->

    <trades on="33" name="Pistonizer"> <!-- Name and material of the block the trade is performed on -->
      <trade>
        <in>gold block</in> <!-- The input -->
        <out amount="3" name="`7Special Name" enchantment="knockback">gold ingot</out> <!-- The output -->
      </trade>
      <trade>
        <in>iron block</in>
        <out amount="3" name="`7Special Name" enchantment="knockback">iron ingot</out>
      </trade>
    </trades>
  
    <trades on="29" name="Pistonizer 2">
      <trade>
        <in>diamond block</in>
        <out amount="76" name="`7Special Name" enchantment="knockback">gold ingot</out>
      </trade>
      <trade>
        <in>iron block</in>
        <out amount="67" name="`7Special Name" enchantment="knockback">iron ingot</out>
      </trade>
    </trades>
  </root>
