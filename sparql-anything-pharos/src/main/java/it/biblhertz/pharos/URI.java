package it.biblhertz.pharos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.damnhandy.uri.template.UriTemplate;

/**
 * This generator is responsible for constructing URIs that contain UUIDs for
 * some of their arguments that will be either (a) the result of the hashing
 * over the contents of a particular argument or (b) a random UUID of a
 * particular argument. It is up to the user to define the desired functionality
 * over every argument by encoding the functionality in the argument name. More
 * specifically: (a) if an argument name ends with _HASHED_CONTENTS then a UUID
 * will be generated based on the contents of the argument (b) if an argument
 * name ends with _RANDOM_UUID then a random UUID will be generated ignoring the
 * contents of this argument (c) if an argument name does not end with any of
 * the above then the contents of the argument will be added as they are.
 * 
 * The separator between different arguments is / character
 * 
 * @author Yannis Marketakis (marketak 'at' ics 'dot' forth 'dot' gr)
 */
public class URI {

	class Labels {
		static final String _HASHED_CONTENTS = "DUMMY_HASHED_CONTENTS";
		static final String _RANDOM_UUID = "DUMMY_RANDOM_UUID";
		static final String URI = "DUMMY_URI";
	}

	List<String> evaluatedArguments = new ArrayList<>();

	/**
	 * Arguments are evaluated in order of definition (as they appear in
	 * generator-policy file)
	 * 
	 * @param name  the name of the argument
	 * @param value the original value of the agument
	 */

	public void setArg(String name, String value) {

		if (name.endsWith(Labels._HASHED_CONTENTS)) {
//            evaluatedArguments.add(UUID.nameUUIDFromBytes(value.getBytes()).toString().toUpperCase());
			String encodedText = UriTemplate.fromTemplate("{foo}").set("foo", value).expand();
			UUID uuid = UUID.nameUUIDFromBytes(encodedText.getBytes());
			// long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
			// String shortenedSuffix=Long.toString(l, Character.MAX_RADIX);
			evaluatedArguments.add(uuid.toString().toUpperCase());
		} else if (name.endsWith(Labels._RANDOM_UUID)) {
//            evaluatedArguments.add(UUID.randomUUID().toString().toUpperCase());
			UUID uuid = UUID.randomUUID();
			// long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
			// String shortenedSuffix=Long.toString(l, Character.MAX_RADIX);
			evaluatedArguments.add(uuid.toString().toUpperCase());
		} else {
			evaluatedArguments.add(value);
		}
	}

	public void usesNamespacePrefix() {
		;
	}

	public String getValue() {
		StringBuilder valueBuilder = new StringBuilder();
		for (String value : evaluatedArguments) {
			valueBuilder.append(value).append("/");
		}
		return valueBuilder.deleteCharAt(valueBuilder.length() - 1).toString();
	}

	public String getValueType() {
		return Labels.URI;
	}

	public boolean mergeMultipleValues() {
		return false;
	}
}