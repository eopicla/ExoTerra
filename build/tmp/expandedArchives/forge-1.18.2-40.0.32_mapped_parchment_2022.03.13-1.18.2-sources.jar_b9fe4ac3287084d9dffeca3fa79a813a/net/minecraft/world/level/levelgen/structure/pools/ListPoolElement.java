package net.minecraft.world.level.levelgen.structure.pools;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ListPoolElement extends StructurePoolElement {
   public static final Codec<ListPoolElement> CODEC = RecordCodecBuilder.create((p_210367_) -> {
      return p_210367_.group(StructurePoolElement.CODEC.listOf().fieldOf("elements").forGetter((p_210369_) -> {
         return p_210369_.elements;
      }), projectionCodec()).apply(p_210367_, ListPoolElement::new);
   });
   private final List<StructurePoolElement> elements;

   public ListPoolElement(List<StructurePoolElement> p_210363_, StructureTemplatePool.Projection p_210364_) {
      super(p_210364_);
      if (p_210363_.isEmpty()) {
         throw new IllegalArgumentException("Elements are empty");
      } else {
         this.elements = p_210363_;
         this.setProjectionOnEachElement(p_210364_);
      }
   }

   public Vec3i getSize(StructureManager pStructureManager, Rotation pRotation) {
      int i = 0;
      int j = 0;
      int k = 0;

      for(StructurePoolElement structurepoolelement : this.elements) {
         Vec3i vec3i = structurepoolelement.getSize(pStructureManager, pRotation);
         i = Math.max(i, vec3i.getX());
         j = Math.max(j, vec3i.getY());
         k = Math.max(k, vec3i.getZ());
      }

      return new Vec3i(i, j, k);
   }

   public List<StructureTemplate.StructureBlockInfo> getShuffledJigsawBlocks(StructureManager pStructureManager, BlockPos pPos, Rotation pRotation, Random pRandom) {
      return this.elements.get(0).getShuffledJigsawBlocks(pStructureManager, pPos, pRotation, pRandom);
   }

   public BoundingBox getBoundingBox(StructureManager pStructureManager, BlockPos pPos, Rotation pRotation) {
      Stream<BoundingBox> stream = this.elements.stream().filter((p_210371_) -> {
         return p_210371_ != EmptyPoolElement.INSTANCE;
      }).map((p_210399_) -> {
         return p_210399_.getBoundingBox(pStructureManager, pPos, pRotation);
      });
      return BoundingBox.encapsulatingBoxes(stream::iterator).orElseThrow(() -> {
         return new IllegalStateException("Unable to calculate boundingbox for ListPoolElement");
      });
   }

   public boolean place(StructureManager p_210378_, WorldGenLevel p_210379_, StructureFeatureManager p_210380_, ChunkGenerator p_210381_, BlockPos p_210382_, BlockPos p_210383_, Rotation p_210384_, BoundingBox p_210385_, Random p_210386_, boolean p_210387_) {
      for(StructurePoolElement structurepoolelement : this.elements) {
         if (!structurepoolelement.place(p_210378_, p_210379_, p_210380_, p_210381_, p_210382_, p_210383_, p_210384_, p_210385_, p_210386_, p_210387_)) {
            return false;
         }
      }

      return true;
   }

   public StructurePoolElementType<?> getType() {
      return StructurePoolElementType.LIST;
   }

   public StructurePoolElement setProjection(StructureTemplatePool.Projection pProjection) {
      super.setProjection(pProjection);
      this.setProjectionOnEachElement(pProjection);
      return this;
   }

   public String toString() {
      return "List[" + (String)this.elements.stream().map(Object::toString).collect(Collectors.joining(", ")) + "]";
   }

   private void setProjectionOnEachElement(StructureTemplatePool.Projection pProjection) {
      this.elements.forEach((p_210376_) -> {
         p_210376_.setProjection(pProjection);
      });
   }
}