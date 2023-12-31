package com.jeffdisher.cacophony.data.global;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.jeffdisher.cacophony.data.global.description.StreamDescription;
import com.jeffdisher.cacophony.data.global.index.StreamIndex;
import com.jeffdisher.cacophony.data.global.recommendations.StreamRecommendations;
import com.jeffdisher.cacophony.data.global.record.DataArray;
import com.jeffdisher.cacophony.data.global.record.DataElement;
import com.jeffdisher.cacophony.data.global.record.ElementSpecialType;
import com.jeffdisher.cacophony.data.global.record.StreamRecord;
import com.jeffdisher.cacophony.data.global.records.StreamRecords;
import com.jeffdisher.cacophony.data.global.v2.description.CacophonyDescription;
import com.jeffdisher.cacophony.data.global.v2.description.MiscData;
import com.jeffdisher.cacophony.data.global.v2.description.PictureReference;
import com.jeffdisher.cacophony.data.global.v2.extensions.CacophonyExtensionVideo;
import com.jeffdisher.cacophony.data.global.v2.extensions.VideoFormat;
import com.jeffdisher.cacophony.data.global.v2.recommendations.CacophonyRecommendations;
import com.jeffdisher.cacophony.data.global.v2.record.CacophonyRecord;
import com.jeffdisher.cacophony.data.global.v2.record.ThumbnailReference;
import com.jeffdisher.cacophony.data.global.v2.records.CacophonyRecords;
import com.jeffdisher.cacophony.data.global.v2.root.CacophonyRoot;
import com.jeffdisher.cacophony.data.global.v2.root.DataReference;
import com.jeffdisher.cacophony.types.IpfsFile;
import com.jeffdisher.cacophony.types.IpfsKey;

import io.ipfs.cid.Cid;


public class TestAbstractWrappers
{
	@Test
	public void emptyRecord() throws Throwable
	{
		// We need to set some minimal values for the encoding to work.
		StreamRecord record = new StreamRecord();
		record.setName("name");
		record.setDescription("description");
		record.setPublishedSecondsUtc(1L);
		record.setPublisherKey(K0.toPublicKey());
		record.setElements(new DataArray());
		byte[] data = GlobalData.serializeRecord(record);
		AbstractRecord middle = AbstractRecord.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals("name", middle.getName());
		Assert.assertEquals("description", middle.getDescription());
		Assert.assertEquals(1L, middle.getPublishedSecondsUtc());
		Assert.assertEquals(K0, middle.getPublisherKey());
		Assert.assertNull(middle.getDiscussionUrl());
		Assert.assertNull(middle.getThumbnailMime());
		Assert.assertEquals(0, middle.getExternalElementCount());
		Assert.assertNull(middle.getVideoExtension());
	}

	@Test
	public void emptyRecordV2() throws Throwable
	{
		// We need to set some minimal values for the encoding to work.
		CacophonyRecord record = new CacophonyRecord();
		record.setName("name");
		record.setPublishedSecondsUtc(1L);
		record.setPublisherKey(K0.toPublicKey());
		byte[] data = GlobalData2.serializeRecord(new CacophonyExtendedRecord(record, null));
		AbstractRecord middle = AbstractRecord.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals("name", middle.getName());
		Assert.assertEquals(1L, middle.getPublishedSecondsUtc());
		Assert.assertEquals(K0, middle.getPublisherKey());
		Assert.assertNull(middle.getDiscussionUrl());
		Assert.assertNull(middle.getThumbnailMime());
		Assert.assertNull(middle.getReplyTo());
		Assert.assertEquals(0, middle.getExternalElementCount());
		Assert.assertNull(middle.getVideoExtension());
	}

	@Test
	public void fullRecord() throws Throwable
	{
		DataElement thumbnail = new DataElement();
		thumbnail.setCid(_generateHash(new byte[] { 1 }).toSafeString());
		thumbnail.setMime("image/jpeg");
		thumbnail.setSpecial(ElementSpecialType.IMAGE);
		DataElement video = new DataElement();
		video.setCid(_generateHash(new byte[] { 2 }).toSafeString());
		video.setMime("video/webm");
		video.setWidth(1);
		video.setHeight(1);
		DataElement audio = new DataElement();
		audio.setCid(_generateHash(new byte[] { 3 }).toSafeString());
		audio.setMime("audio/ogg");
		audio.setWidth(0);
		audio.setHeight(0);
		DataArray elements = new DataArray();
		elements.getElement().add(thumbnail);
		elements.getElement().add(video);
		elements.getElement().add(audio);
		StreamRecord record = new StreamRecord();
		record.setName("name");
		record.setDescription("description");
		record.setPublishedSecondsUtc(1L);
		record.setPublisherKey(K0.toPublicKey());
		record.setDiscussion("http://example.com");
		record.setElements(elements);
		byte[] data = GlobalData.serializeRecord(record);
		AbstractRecord middle = AbstractRecord.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals("name", middle.getName());
		Assert.assertEquals("description", middle.getDescription());
		Assert.assertEquals(1L, middle.getPublishedSecondsUtc());
		Assert.assertEquals(K0, middle.getPublisherKey());
		Assert.assertEquals("http://example.com", middle.getDiscussionUrl());
		Assert.assertEquals("image/jpeg", middle.getThumbnailMime());
		Assert.assertEquals(_generateHash(new byte[] { 1 }), middle.getThumbnailCid());
		Assert.assertEquals(3, middle.getExternalElementCount());
		Assert.assertEquals(2, middle.getVideoExtension().size());
	}

	@Test
	public void fullRecordV2() throws Throwable
	{
		CacophonyExtensionVideo extension = new CacophonyExtensionVideo();
		ThumbnailReference thumbnail = new ThumbnailReference();
		thumbnail.setValue(_generateHash(new byte[] { 1 }).toSafeString());
		thumbnail.setMime("image/jpeg");
		VideoFormat video = new VideoFormat();
		video.setCid(_generateHash(new byte[] { 2 }).toSafeString());
		video.setMime("video/webm");
		video.setWidth(1);
		video.setHeight(1);
		VideoFormat audio = new VideoFormat();
		audio.setCid(_generateHash(new byte[] { 3 }).toSafeString());
		audio.setMime("audio/ogg");
		audio.setWidth(0);
		audio.setHeight(0);
		extension.getFormat().add(video);
		extension.getFormat().add(audio);
		CacophonyRecord record = new CacophonyRecord();
		record.setName("name");
		record.setDescription("description");
		record.setPublishedSecondsUtc(1L);
		record.setPublisherKey(K0.toPublicKey());
		record.setDiscussionUrl("http://example.com");
		record.setThumbnail(thumbnail);
		byte[] data = GlobalData2.serializeRecord(new CacophonyExtendedRecord(record, extension));
		AbstractRecord middle = AbstractRecord.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals("name", middle.getName());
		Assert.assertEquals("description", middle.getDescription());
		Assert.assertEquals(1L, middle.getPublishedSecondsUtc());
		Assert.assertEquals(K0, middle.getPublisherKey());
		Assert.assertEquals("http://example.com", middle.getDiscussionUrl());
		Assert.assertEquals("image/jpeg", middle.getThumbnailMime());
		Assert.assertEquals(_generateHash(new byte[] { 1 }), middle.getThumbnailCid());
		Assert.assertEquals(3, middle.getExternalElementCount());
		Assert.assertEquals(2, middle.getVideoExtension().size());
	}

	@Test
	public void emptyRecords() throws Throwable
	{
		StreamRecords record = new StreamRecords();
		byte[] data = GlobalData.serializeRecords(record);
		AbstractRecords middle = AbstractRecords.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
	}

	@Test
	public void emptyRecordsV2() throws Throwable
	{
		CacophonyRecords record = new CacophonyRecords();
		byte[] data = GlobalData2.serializeRecords(record);
		AbstractRecords middle = AbstractRecords.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
	}

	@Test
	public void smallRecords() throws Throwable
	{
		StreamRecords records = new StreamRecords();
		records.getRecord().add(_generateHash(new byte[] { 1 }).toSafeString());
		byte[] data = GlobalData.serializeRecords(records);
		AbstractRecords middle = AbstractRecords.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals(1, middle.getRecordList().size());
		Assert.assertEquals(_generateHash(new byte[] { 1 }), middle.getRecordList().get(0));
	}

	@Test
	public void smallRecordsV2() throws Throwable
	{
		CacophonyRecords records = new CacophonyRecords();
		records.getRecord().add(_generateHash(new byte[] { 1 }).toSafeString());
		byte[] data = GlobalData2.serializeRecords(records);
		AbstractRecords middle = AbstractRecords.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals(1, middle.getRecordList().size());
		Assert.assertEquals(_generateHash(new byte[] { 1 }), middle.getRecordList().get(0));
	}

	@Test
	public void emptyRecommendations() throws Throwable
	{
		StreamRecommendations recommendations = new StreamRecommendations();
		byte[] data = GlobalData.serializeRecommendations(recommendations);
		AbstractRecommendations middle = AbstractRecommendations.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
	}

	@Test
	public void emptyRecommendationsV2() throws Throwable
	{
		CacophonyRecommendations recommendations = new CacophonyRecommendations();
		byte[] data = GlobalData2.serializeRecommendations(recommendations);
		AbstractRecommendations middle = AbstractRecommendations.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
	}

	@Test
	public void smallRecommendations() throws Throwable
	{
		StreamRecommendations recommendations = new StreamRecommendations();
		recommendations.getUser().add(K0.toPublicKey());
		byte[] data = GlobalData.serializeRecommendations(recommendations);
		AbstractRecommendations middle = AbstractRecommendations.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals(1, middle.getUserList().size());
		Assert.assertEquals(K0, middle.getUserList().get(0));
	}

	@Test
	public void smallRecommendationsV2() throws Throwable
	{
		CacophonyRecommendations recommendations = new CacophonyRecommendations();
		recommendations.getUser().add(K0.toPublicKey());
		byte[] data = GlobalData2.serializeRecommendations(recommendations);
		AbstractRecommendations middle = AbstractRecommendations.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals(1, middle.getUserList().size());
		Assert.assertEquals(K0, middle.getUserList().get(0));
	}

	@Test
	public void emptyDescription() throws Throwable
	{
		// We need to set some minimal values for the encoding to work.
		StreamDescription description = new StreamDescription();
		description.setName("name");
		description.setDescription("description");
		description.setPicture(_generateHash(new byte[] { 1 }).toSafeString());
		byte[] data = GlobalData.serializeDescription(description);
		AbstractDescription middle = AbstractDescription.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals("name", middle.getName());
		Assert.assertEquals("description", middle.getDescription());
		Assert.assertEquals(_generateHash(new byte[] { 1 }), middle.getPicCid());
		// We use "image/jpeg" as the synthetic mime.
		Assert.assertEquals("image/jpeg", middle.getPicMime());
	}

	@Test
	public void emptyDescriptionV2() throws Throwable
	{
		// We need to set some minimal values for the encoding to work.
		CacophonyDescription description = new CacophonyDescription();
		description.setName("name");
		description.setDescription("description");
		PictureReference ref = new PictureReference();
		ref.setMime("image/jpeg");
		ref.setValue(_generateHash(new byte[] { 1 }).toSafeString());
		description.setPicture(ref);
		byte[] data = GlobalData2.serializeDescription(description);
		AbstractDescription middle = AbstractDescription.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals("name", middle.getName());
		Assert.assertEquals("description", middle.getDescription());
		Assert.assertEquals(_generateHash(new byte[] { 1 }), middle.getPicCid());
		// We use "image/jpeg" as the synthetic mime.
		Assert.assertEquals("image/jpeg", middle.getPicMime());
	}

	@Test
	public void fullDescription() throws Throwable
	{
		StreamDescription description = new StreamDescription();
		description.setName("name");
		description.setDescription("description");
		description.setEmail("test@example.com");
		description.setWebsite("http://example.com");
		description.setPicture(_generateHash(new byte[] { 1 }).toSafeString());
		byte[] data = GlobalData.serializeDescription(description);
		AbstractDescription middle = AbstractDescription.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals("name", middle.getName());
		Assert.assertEquals("description", middle.getDescription());
		Assert.assertEquals(_generateHash(new byte[] { 1 }), middle.getPicCid());
		Assert.assertEquals("image/jpeg", middle.getPicMime());
		Assert.assertEquals("test@example.com", middle.getEmail());
		Assert.assertEquals("http://example.com", middle.getWebsite());
	}

	@Test
	public void fullDescriptionV2() throws Throwable
	{
		CacophonyDescription description = new CacophonyDescription();
		description.setName("name");
		description.setDescription("description");
		MiscData email = new MiscData();
		email.setType(GlobalData2.DESCRIPTION_MISC_TYPE_EMAIL);
		email.setValue("test@example.com");
		description.getMisc().add(email);
		MiscData website = new MiscData();
		website.setType(GlobalData2.DESCRIPTION_MISC_TYPE_WEBSITE);
		website.setValue("http://example.com");
		description.getMisc().add(website);
		PictureReference pictureRef = new PictureReference();
		pictureRef.setMime("image/jpeg");
		pictureRef.setValue(_generateHash(new byte[] { 1 }).toSafeString());
		description.setPicture(pictureRef);
		description.setFeature(_generateHash(new byte[] {2}).toSafeString());
		byte[] data = GlobalData2.serializeDescription(description);
		AbstractDescription middle = AbstractDescription.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals("name", middle.getName());
		Assert.assertEquals("description", middle.getDescription());
		Assert.assertEquals(_generateHash(new byte[] { 1 }), middle.getPicCid());
		Assert.assertEquals("image/jpeg", middle.getPicMime());
		Assert.assertEquals("test@example.com", middle.getEmail());
		Assert.assertEquals("http://example.com", middle.getWebsite());
		Assert.assertEquals(_generateHash(new byte[] { 2 }), middle.getFeature());
	}

	@Test
	public void basicIndex() throws Throwable
	{
		IpfsFile description = _generateHash(new byte[] { 1 });
		IpfsFile recommendations = _generateHash(new byte[] { 2 });
		IpfsFile records = _generateHash(new byte[] { 3 });
		StreamIndex index = new StreamIndex();
		index.setVersion(1);
		index.setDescription(description.toSafeString());
		index.setRecommendations(recommendations.toSafeString());
		index.setRecords(records.toSafeString());
		byte[] data = GlobalData.serializeIndex(index);
		AbstractIndex middle = AbstractIndex.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV1();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals(description, middle.descriptionCid);
		Assert.assertEquals(recommendations, middle.recommendationsCid);
		Assert.assertEquals(records, middle.recordsCid);
	}

	@Test
	public void basicIndexV2() throws Throwable
	{
		IpfsFile description = _generateHash(new byte[] { 1 });
		IpfsFile recommendations = _generateHash(new byte[] { 2 });
		IpfsFile records = _generateHash(new byte[] { 3 });
		CacophonyRoot index = new CacophonyRoot();
		index.setVersion(2);
		DataReference data_description = new DataReference();
		data_description.setType(GlobalData2.ROOT_DATA_TYPE_DESCRIPTION);
		data_description.setValue(description.toSafeString());
		index.getData().add(data_description);
		DataReference data_recommendations = new DataReference();
		data_recommendations.setType(GlobalData2.ROOT_DATA_TYPE_RECOMMENDATIONS);
		data_recommendations.setValue(recommendations.toSafeString());
		index.getData().add(data_recommendations);
		DataReference data_records = new DataReference();
		data_records.setType(GlobalData2.ROOT_DATA_TYPE_RECORDS);
		data_records.setValue(records.toSafeString());
		index.getData().add(data_records);
		byte[] data = GlobalData2.serializeRoot(index);
		AbstractIndex middle = AbstractIndex.DESERIALIZER.apply(data);
		byte[] data2 = middle.serializeV2();
		Assert.assertArrayEquals(data, data2);
		
		Assert.assertEquals(description, middle.descriptionCid);
		Assert.assertEquals(recommendations, middle.recommendationsCid);
		Assert.assertEquals(records, middle.recordsCid);
	}


	private static final IpfsKey K0 = IpfsKey.fromPublicKey("z5AanNVJCxnSSsLjo4tuHNWSmYs3TXBgKWxVqdyNFgwb1br5PBWo14Y");

	private static IpfsFile _generateHash(byte[] data)
	{
		int hashCode = Arrays.hashCode(data);
		byte[] hash = new byte[34];
		ByteBuffer buffer = ByteBuffer.wrap(hash);
		buffer.put((byte)18).put((byte)32);
		buffer.putInt(hashCode).putInt(hashCode).putInt(hashCode).putInt(hashCode);
		return IpfsFile.fromIpfsCid(Cid.cast(hash).toString());
	}
}
