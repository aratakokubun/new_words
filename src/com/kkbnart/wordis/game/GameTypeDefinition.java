package com.kkbnart.wordis.game;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkbnart.wordis.Constants;
import com.kkbnart.wordis.game.exception.LoadPropertyException;

public class GameTypeDefinition {
	// Board parameters
	public int boardRow, boardCol;
	public int boardCollisionX, boardCollisionY;
	public int boardCollisionRow, boardCollisionCol;
	public float boardXRate, boardYRate;
	public float boardWRate, boardHRate;

	// Operated block parameters
	public int operatedX, operatedY;
	
	// Next block parameters
	public float nextX, nextY;
	public float nextMarginX, nextMarginY;
	public int nextSize;
	
	public void loadProperty(final String propertyName) throws LoadPropertyException {
		final ObjectMapper mapper = new ObjectMapper();
		try {
			JsonNode root = mapper.readTree(new File(Constants.GAME_TYPE_JSON));
			root = root.get(propertyName);
			// Board parameters
			final JsonNode boardRoot = root.get("board");
			boardRow = boardRoot.get("row").asInt();
			boardCol = boardRoot.get("col").asInt();
			boardCollisionX = boardRoot.get("collisionX").asInt();
			boardCollisionY = boardRoot.get("collisionY").asInt();
			boardCollisionRow = boardRoot.get("collisionRow").asInt();
			boardCollisionCol = boardRoot.get("collisionCol").asInt();
			boardXRate = (float)boardRoot.get("xRate").asDouble();
			boardYRate = (float)boardRoot.get("yRate").asDouble();
			boardWRate = (float)boardRoot.get("wRate").asDouble();
			boardHRate = (float)boardRoot.get("hRate").asDouble();
			// Operated block parameters
			final JsonNode operatedRoot = root.get("operated");
			operatedX = operatedRoot.get("x").asInt();
			operatedY = operatedRoot.get("y").asInt();
			// Next block parameters
			final JsonNode nextRoot = root.get("next");
			nextX = (float)nextRoot.get("x").asDouble();
			nextY = (float)nextRoot.get("y").asDouble();
			nextMarginX = (float)nextRoot.get("marginX").asDouble();
			nextMarginY = (float)nextRoot.get("marginY").asDouble();
			nextSize = nextRoot.get("size").asInt();
		} catch (Exception e) {
			System.out.println(e);
			throw new LoadPropertyException();
		}
	}
}
