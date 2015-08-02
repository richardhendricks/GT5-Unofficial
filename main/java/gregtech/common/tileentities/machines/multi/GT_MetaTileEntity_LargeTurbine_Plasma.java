package gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class GT_MetaTileEntity_LargeTurbine_Plasma extends GT_MetaTileEntity_LargeTurbine{

	public GT_MetaTileEntity_LargeTurbine_Plasma(int aID, String aName, String aNameRegional){super(aID, aName, aNameRegional);}
	public GT_MetaTileEntity_LargeTurbine_Plasma(String aName){super(aName);}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[1][aColorIndex+1], aFacing == aSide ? aActive ? new GT_RenderedTexture(Textures.BlockIcons.LARGETURBINE_ACTIVE5):new GT_RenderedTexture(Textures.BlockIcons.LARGETURBINE5) : Textures.BlockIcons.CASING_BLOCKS[57]};
	}
	
	   
	   public String[] getDescription()
	   {
	     return new String[] { 
	    		 "Controller Block for the Large Plasma Generator",
	    		 "Size: 3x3x4 (Hollow)", "Controller (front centered)",
	    		 "1x Input Hatch (side centered)",
	    		 "1x Dynamo Hatch (back centered)", 
	    		 "1x Maintenance Hatch (side centered)", 
	    		 "Turbine Casings for the rest (24 at least!)",
	    		 "Needs a Turbine Item (inside controller GUI)" };
	   }
	   	
		public int getFuelValue(FluidStack aLiquid) {
	    	if (aLiquid == null || GT_Recipe_Map.sTurbineFuels == null) return 0;
	    	FluidStack tLiquid;
	    	Collection<GT_Recipe> tRecipeList = GT_Recipe_Map.sPlasmaFuels.mRecipeList;
	    	if (tRecipeList != null) for (GT_Recipe tFuel : tRecipeList) if ((tLiquid = GT_Utility.getFluidForFilledItem(tFuel.getRepresentativeInput(0), true)) != null) if (aLiquid.isFluidEqual(tLiquid)) return tFuel.mSpecialValue;
	    	return 0;
	    }
		
		@Override
		public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {return new GT_MetaTileEntity_LargeTurbine_Plasma(mName);}
		@Override
		public Block getCasingBlock() {
			return GregTech_API.sBlockCasings4;
		}
		@Override
		public byte getCasingMeta() {
			return 9;
		}
		@Override
		public byte getCasingTextureIndex() {
			return 46;
		}
		@Override
		public int getPollutionPerTick(ItemStack aStack) {
			return 0;
		}
		
		@Override
		int fluidIntoPower(ArrayList<FluidStack> aFluids, int aOptFlow,	int aBaseEff) {
		    int tEU=0;
		    int tOut=0;
		    int tOptFlow = aOptFlow * 40;
		    boolean b = false;
		    for(int i=0;i<aFluids.size();i++){
		    	int fuelValue = getFuelValue(aFluids.get(i));
		    	if(fuelValue>0&&depleteInput(new FluidStack(aFluids.get(i),Math.max(tOptFlow/(fuelValue*2),1)))){
		    		tEU += tOptFlow/2;}
		    }
		    if(tEU>0)b=true;
		    tEU = getAverage(tEU);
		    if(b&&tEU<=0)tEU=3;
			return tEU * aBaseEff / 10000;
		}


}
