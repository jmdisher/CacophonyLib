package com.jeffdisher.cacophony.utils;

import org.junit.Test;


public class TestMiscHelpers
{
	@Test
	public void testSmall()
	{
		long bytes = 657l;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("657 bytes", readable);
	}

	@Test
	public void testKEven()
	{
		long bytes = 2_000l;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("2.00 kB (2000 bytes)", readable);
	}

	@Test
	public void testKOdd()
	{
		long bytes = 3_567l;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("3.57 kB (3567 bytes)", readable);
	}

	@Test
	public void testMEven()
	{
		long bytes = 2_000_000l;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("2.00 MB (2000000 bytes)", readable);
	}

	@Test
	public void testMOdd()
	{
		long bytes = 3_987_567l;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("3.99 MB (3987567 bytes)", readable);
	}

	@Test
	public void testGEven()
	{
		long bytes = 2_000_000_000l;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("2.00 GB (2000000000 bytes)", readable);
	}

	@Test
	public void testGOdd()
	{
		long bytes = 3_123_987_567l;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("3.12 GB (3123987567 bytes)", readable);
	}

	@Test
	public void testIndirectOverflow()
	{
		long bytes = 3_999_987_567l;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("4.00 GB (3999987567 bytes)", readable);
	}

	@Test
	public void test10G()
	{
		long bytes = 10_000_000_000L;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("10.00 GB (10000000000 bytes)", readable);
	}

	@Test
	public void testRounding()
	{
		long bytes = 2_097_152L;
		String readable = MiscHelpers.humanReadableBytes(bytes);
		org.junit.Assert.assertEquals("2.10 MB (2097152 bytes)", readable);
	}
}
