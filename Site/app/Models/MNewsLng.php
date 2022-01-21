<?php

namespace App\Models;

use App\Models\MNews;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $M_NewsL_id
 * @property int        $M_News_id
 * @property int        $C_Lang_id
 * @property string     $M_NewsL_title
 * @property string     $M_NewsL_int
 * @property string     $M_NewsL_body
 * @property string     $M_NewsL_script
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class MNewsLng extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'M_News_Lng';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'M_NewsL_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'M_News_id', 'C_Lang_id', 'M_NewsL_title', 'M_NewsL_int', 'M_NewsL_body', 'M_NewsL_script', 'C_St_id'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'M_NewsL_id' => 'int', 'M_News_id' => 'int', 'C_Lang_id' => 'int', 'M_NewsL_title' => 'string', 'M_NewsL_int' => 'string', 'M_NewsL_body' => 'string', 'M_NewsL_script' => 'string', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_news()
    {
        return $this->belongsTo(MNews::class, 'M_News_id');
    }

    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
}
