import org.intellij.lang.annotations.Language

/**
 *
 *
 */
@Language("AGSL")
val GlitchShader =
    """
        uniform float2 resolution;
        uniform float time;
        uniform shader contents;
//        uniform sampler2D sampler;

//        vec4 main(in vec2 fragCoord) {
//    	vec2 uv = fragCoord.xy / resolution.xy * 0.8 + 0.1;
//
//        uv += sin(time * vec2(1.0, 2.0) + uv* 2.0) * 0.01;
//
//        return contents.eval(uv * resolution.xy);
//    


        float glitch_power = 0.04;
        float glitch_speed = 5.0;
        float glitch_dist = 30.5;
        float glitch_rate  = 0.2;
        float glitch_color_rate  = 0.01;

        float random( float seed )
        {
            return fract( 543.2543 * sin( dot( vec2( seed, seed ), vec2( 3525.46, -54.3415 ) ) ) );
        }
        
        float truncate(float x) {
            if (x >= 0.0) {
                return floor(x);
            } else {
                return floor(x + 1.0);
            }
        }


        vec4 main(vec2 fragCoord)
        {
            float enable_shift = float(
                random( truncate( time * glitch_speed ) )
                < glitch_rate
            );

            vec2 uv = (fragCoord / resolution.xy) ;
//* 0.8 + 0.1;

            vec2 fixed_uv = uv;
            fixed_uv.x += (
                random(
                    ( truncate( uv.y * glitch_dist ) / glitch_dist )
                    + time
                ) - 0.5 
            ) * glitch_power * enable_shift;

            return contents.eval( fixed_uv * resolution.xy);


//            vec4 pixel_color = texture2D( sampler, fixed_uv );
//            pixel_color.r = mix(
//                pixel_color.r,
//                texture2D( sampler, fixed_uv + vec2( glitch_color_rate, 0.0 ) ).r,
//                enable_shift
//            );
//            pixel_color.b = mix(
//                pixel_color.b,
//                texture2D( sampler, fixed_uv + vec2( -glitch_color_rate, 0.0 ) ).b,
//                enable_shift
//            );
//            ag_FragColor = pixel_color;
        }
    """.trimIndent()