package com.yfdl.dto.collect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsertCollectDto {
   public List<Long> collectionId;
   public Long articleId;
}
