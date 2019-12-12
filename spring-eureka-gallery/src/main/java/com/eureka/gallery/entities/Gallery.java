package com.eureka.gallery.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Gallery {

	private final int id;
	private List<Object> images;
}